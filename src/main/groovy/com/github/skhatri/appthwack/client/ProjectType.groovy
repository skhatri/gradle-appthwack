package com.github.skhatri.appthwack.client

enum ProjectType {
    ANDROID, WEB, IOS

    public static ProjectType fromOrdinal(int ordinal) {
        for(ProjectType projectType: values()) {
            if(projectType.ordinal() == ordinal) {
                return projectType
            }
        }
        null
    }

}
