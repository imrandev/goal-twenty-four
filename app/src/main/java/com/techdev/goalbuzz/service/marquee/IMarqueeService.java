package com.techdev.goalbuzz.service.marquee;

import java.util.List;

public interface IMarqueeService<T> {
    String get(List<T>... list);
    String build(List<T> list, String type);
    String getLive(List<T> list, String type);
    String getUpcoming(List<T> list, String type);
    String getResult(List<T> list, String type);
}
