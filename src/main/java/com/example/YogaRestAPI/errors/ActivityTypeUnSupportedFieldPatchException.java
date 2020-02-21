package com.example.YogaRestAPI.errors;

import java.util.Set;

public class ActivityTypeUnSupportedFieldPatchException extends RuntimeException {
    public ActivityTypeUnSupportedFieldPatchException(Set<String> fields) {
        super("Field " + fields.toString() + " update is not allow.");
    }
}
