package com.cst.im.presenter;

import com.cst.im.model.IFriend;

/**
 * Created by sun on 2017/5/4.
 */

public interface IFriendPresenter {
    void Getfriendlist(int id);
    void Isfriend(int ownerid,int IsFriendId);
    void handleIsFriendEvent(final IFriend msgRecv);
    void AddFriend(int ownerid,int friendid);
    void handleAddFriendEvent(final IFriend msgRecv);
}
