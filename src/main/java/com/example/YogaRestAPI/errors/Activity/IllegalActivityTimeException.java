package com.example.YogaRestAPI.errors.Activity;

import com.example.YogaRestAPI.domain.Activity;

public class IllegalActivityTimeException extends RuntimeException{
    public IllegalActivityTimeException(Activity activity, Activity activityFromDb) {
        super(buildMessage(activity, activityFromDb));
    }

    private static String buildMessage(Activity activity, Activity activityFromDb) {
        StringBuilder message = new StringBuilder();
        message.append("Check activity time - 30 minutes between activities: ")
                .append(activity.getStartTime())
                .append(" ")
                .append(activity.getFinishTime())
                .append(". We have the same time activity in schedule ")
                .append(activityFromDb.getName())
                .append(" ")
                .append(activityFromDb.getStartTime())
                .append(" ")
                .append(activityFromDb.getFinishTime());
        return message.toString();
    }
}
