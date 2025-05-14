package com.github.kevin.learning.learning_1;

import lombok.RequiredArgsConstructor;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class DelayQueueTest implements Delayed {

    // 任务名
    private final String taskName;
    // 延迟时间(代表该任务延迟多少时间后执行，单位ms)
    private final long delayTime;

    private final long currentTime;

    @Override
    public long getDelay(TimeUnit unit) {
        // 返回剩余延迟时间，当返回值<=0时表示元素已到期
        return unit.convert(delayTime+currentTime-System.currentTimeMillis(), TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        // 用于排序，确保最早到期的元素排在队列前面
        return Long.compare(this.delayTime, ((DelayQueueTest) o).delayTime);
    }

    @Override
    public String toString() {
        return "DelayQueueTest{" +
                "taskName='" + taskName + '\'' +
                ", delayTime=" + delayTime +
                '}';
    }
}
