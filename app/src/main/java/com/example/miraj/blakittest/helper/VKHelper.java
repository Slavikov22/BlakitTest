package com.example.miraj.blakittest.helper;

import android.content.Context;

import com.example.miraj.blakittest.R;
import com.vk.sdk.api.model.VKApiUserFull;

public class VKHelper {
    public static String getVKUserFieldsAsString(String... fields) {
        StringBuilder result = new StringBuilder();
        for (String s : fields)
            result.append(s).append(",");
        return result.toString();
    }

    public static String[] getAvailableFamilyStatuses(Context context, VKApiUserFull user) {
        if (user.sex == VKApiUserFull.Sex.MALE)
            return new String[] {
                    context.getString(R.string.notChoose),
                    context.getString(R.string.notMarriedM),
                    context.getString(R.string.hasFriendM),
                    context.getString(R.string.betrothedM),
                    context.getString(R.string.marriedM),
                    context.getString(R.string.allHard),
                    context.getString(R.string.inActiveSearch),
                    context.getString(R.string.inLoveM),
                    context.getString(R.string.civilMarried)
            };
        else if (user.sex == VKApiUserFull.Sex.FEMALE) {
            return new String[] {
                    context.getString(R.string.notChoose),
                    context.getString(R.string.notMarriedF),
                    context.getString(R.string.hasFriendF),
                    context.getString(R.string.betrothedF),
                    context.getString(R.string.marriedF),
                    context.getString(R.string.allHard),
                    context.getString(R.string.inActiveSearch),
                    context.getString(R.string.inLoveF),
                    context.getString(R.string.civilMarried)
            };
        }

        return new String[] {};
    }

    public static String getFamilyStatus(Context context, VKApiUserFull user) {
        return getAvailableFamilyStatuses(context, user)[user.relation];
    }
}
