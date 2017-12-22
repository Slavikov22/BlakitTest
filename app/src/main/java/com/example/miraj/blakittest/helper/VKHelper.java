package com.example.miraj.blakittest.helper;

import android.content.Context;

import com.example.miraj.blakittest.R;
import com.vk.sdk.api.model.VKApiUserFull;

public class VKHelper {
    public static String getFamilyStatus(Context context, VKApiUserFull user) {
        if (user.sex == VKApiUserFull.Sex.MALE) {
            switch (user.relation) {
                case 1: return context.getString(R.string.notMarriedM);
                case 2: return context.getString(R.string.hasFriendM);
                case 3: return context.getString(R.string.betrothedM);
                case 4: return context.getString(R.string.marriedM);
                case 5: return context.getString(R.string.allHard);
                case 6: return context.getString(R.string.inActiveSearch);
                case 7: return context.getString(R.string.inLoveM);
                case 8: return context.getString(R.string.civilMarried);
                default: return "";
            }
        }
        else {
            switch (user.relation) {
                case 1: return context.getString(R.string.notMarriedF);
                case 2: return context.getString(R.string.hasFriendF);
                case 3: return context.getString(R.string.betrothedF);
                case 4: return context.getString(R.string.marriedF);
                case 5: return context.getString(R.string.allHard);
                case 6: return context.getString(R.string.inActiveSearch);
                case 7: return context.getString(R.string.inLoveF);
                case 8: return context.getString(R.string.civilMarried);
                default: return "";
            }
        }
    }
}
