
package com.aibank.framework.sentinellimit.slot;

import com.alibaba.csp.sentinel.log.RecordLog;
import com.alibaba.csp.sentinel.slotchain.*;
import com.alibaba.csp.sentinel.slots.logger.LogSlot;
import com.alibaba.csp.sentinel.spi.Spi;
import com.alibaba.csp.sentinel.spi.SpiLoader;

import java.util.List;

/**
 * Builder for a default {@link ProcessorSlotChain}.
 *
 * @author qinan.qn
 * @author leyou
 */
@Spi(isDefault = false)
public class CustomerSlotChainBuilder implements SlotChainBuilder {

    @Override
    public ProcessorSlotChain build() {
        ProcessorSlotChain chain = new DefaultProcessorSlotChain();

        List<ProcessorSlot> sortedSlotList = SpiLoader.of(ProcessorSlot.class).loadInstanceListSorted();
        for (ProcessorSlot slot : sortedSlotList) {
            if (!(slot instanceof AbstractLinkedProcessorSlot)) {
                RecordLog.warn("The ProcessorSlot(" + slot.getClass().getCanonicalName() + ") is not an instance of AbstractLinkedProcessorSlot, can't be added into ProcessorSlotChain");
                continue;
            }
            if (slot instanceof LogSlot) {
                continue;
            }
            chain.addLast((AbstractLinkedProcessorSlot<?>) slot);
        }

        return chain;
    }
}
