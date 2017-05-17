package com.cst.im.UI.main.chat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.platform.comapi.location.CoordinateType;
import com.cst.im.R;
import com.google.protobuf.DescriptorProtos;

public class LocationActivity extends AppCompatActivity {
    MapView mMapView = null;
    Button sendButton = null;

    static BDLocation lastLocation = null;

    public LocationClient mLocationClient;

    private BaiduMap baiduMap;

    private boolean isFirstLocate = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
        SDKInitializer.setCoordType(CoordType.GCJ02);
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(new LocationListener());
        sendButton = (Button) findViewById(R.id.btn_location_send);
        requestLocation();

        getSupportActionBar().hide();//隐藏标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); // 隐藏状态栏

        setContentView(R.layout.activity_baidumap);
        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.bmapView);
        baiduMap = mMapView.getMap();
        baiduMap.setMyLocationEnabled(true);
        sendButton.setOnClickListener((View.OnClickListener) this);
    }
    private void requestLocation(){
        initLocation();
        mLocationClient.start();
    }
    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setScanSpan(5000);
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
        sendButton.setVisibility(View.GONE);
    }
    private void navigateTo(BDLocation location){
        //Log.d("LOCATIONADD",location.getAddrStr());
        String address = location.getAddrStr();
        if(isFirstLocate){
            Log.d("NAVIGATETO","enter");
            LatLng ll = new LatLng(location.getLatitude(),location.getLongitude());
            creatMark(ll);
            MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(ll);
            baiduMap.animateMapStatus(update);
            update = MapStatusUpdateFactory.zoomTo(18f);
            baiduMap.animateMapStatus(update);
            isFirstLocate = false;
        }
        MyLocationData.Builder builder = new MyLocationData.Builder();
        builder.latitude(location.getLatitude());
        builder.longitude(location.getLongitude());
        MyLocationData locationData = builder.build();
        baiduMap.setMyLocationData(locationData);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
        baiduMap.setMyLocationEnabled(false);
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    private void creatMark(LatLng point){
        //定义Maker坐标点为 point
        //构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.icon_marka);
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(point)
                .icon(bitmap);
        //在地图上添加Marker，并显示

        baiduMap.addOverlay(option);
    }

    public class LocationListener implements BDLocationListener{

        @Override
        public void onReceiveLocation(BDLocation location) {

            sendButton.setEnabled(true);

            Log.d("LOCTYPE",location.getLocType()+"");
            if(location.getLocType() == BDLocation.TypeNetWorkLocation
                    ||location.getLocType() == BDLocation.TypeGpsLocation){

                navigateTo(location);
                Log.d("LONGITUDE",Double.toString(location.getLongitude()));

            }
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    }
    public void sendLocation(View view) {
        Intent intent = this.getIntent();
        intent.putExtra("latitude", lastLocation.getLatitude());
        intent.putExtra("longitude", lastLocation.getLongitude());
        intent.putExtra("address", lastLocation.getAddrStr());
        this.setResult(RESULT_OK, intent);
        finish();
        overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
    }

}

