package com.github.skhatri.appthwack.plugin

import org.joda.time.LocalDateTime
import org.joda.time.format.DateTimeFormat

class FileAddedDateComparator implements Comparator<Map> {

    def FORMAT = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")

    @Override
    int compare(Map o1, Map o2) {
        if (o1 == null || o2 == null)
            0
        else {
            LocalDateTime d1 = LocalDateTime.parse(o1.get("added"), FORMAT)
            LocalDateTime d2 = LocalDateTime.parse(o2.get("added"), FORMAT)
            d2.compareTo(d1)
        }
    }

}
