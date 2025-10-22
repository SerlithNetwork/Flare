package co.technove.flare.live;

import co.technove.flare.internal.util.DoubleArrayList;

import java.util.*;
import java.util.concurrent.Executor;
import java.util.function.Function;

public abstract class PolledCollector extends Collector implements Runnable {
    private final Map<CollectorData, DoubleArrayList> data = new HashMap<>();
    private final Executor executor;

    public PolledCollector(Executor executor, CollectorData... data) {
        this.executor = executor;
        for (CollectorData datum : data) {
            this.data.put(datum, new DoubleArrayList());
        }
    }

    public void collect() {
        this.executor.execute(this);
    }

    @Override
    public Collection<CollectorData> getDataTypes() {
        return this.data.keySet();
    }

    protected void report(CollectorData collectorData, double data) {
        synchronized (this.data) {
            Objects.requireNonNull(this.data.get(collectorData)).addDouble(data);
        }
    }

    public <T> T useDataThenClear(Function<Map<CollectorData, DoubleArrayList>, T> function) {
        synchronized (this.data) {
            T returned = function.apply(this.data);
            this.data.values().forEach(List::clear);
            return returned;
        }
    }

}
