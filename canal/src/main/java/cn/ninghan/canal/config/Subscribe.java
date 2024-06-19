package cn.ninghan.canal.config;

import cn.ninghan.canal.service.ClassroomNumberHandler;
import cn.ninghan.canal.service.ClassroomSeatHandler;
import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.Message;
import com.alibaba.otter.canal.protocol.CanalEntry.Entry;
import com.google.protobuf.InvalidProtocolBufferException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.net.InetSocketAddress;
import java.util.List;

@Service
@Slf4j
public class Subscribe implements ApplicationListener {
    @Resource
    private ClassroomSeatHandler classroomSeatHandler;
    @Resource
    private ClassroomNumberHandler classroomNumberHandler;

    public void CanalSubscribe(){
        CanalConnector canalConnector = CanalConnectors.newSingleConnector(new InetSocketAddress("localhost",
                11111),"train","","");
        new Thread(()->{
            try {
                int batchSize =1000;
                int emptyCount = 0;
                int totalEmptyCount = 100;
                canalConnector.connect();
                canalConnector.subscribe(".*\\..*");
                canalConnector.rollback();
                while (true){
                    if(emptyCount>=totalEmptyCount){
                        emptyCount=0;
                        Thread.sleep(50);
                    }
                    Message message = canalConnector.getWithoutAck(batchSize);
                    long id = message.getId();
                    int size = message.getEntries().size();
                    if(id==-1||size==0){
                        emptyCount++;
                        continue;
                    }
                    emptyCount=0;
                    try{
                        System.out.println("handle");
                        handleEntry(message.getEntries());
                        canalConnector.ack(id);
                    }catch (Exception e){
                        log.error("处理消息 {}异常,siez {}",id,size);
                        canalConnector.rollback(id); // 处理失败, 回滚数据
                        Thread.sleep(10000);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    Thread.sleep(1000);
                    CanalSubscribe();
                }catch (InterruptedException interruptedException){
                    interruptedException.printStackTrace();
                }
                canalConnector.disconnect();
            }
        }).start();
    }

    private void handleEntry(List<Entry> entryList){
        for (Entry entry:entryList){
            if(entry.getEntryType()== CanalEntry.EntryType.TRANSACTIONBEGIN || entry.getEntryType()== CanalEntry.EntryType.TRANSACTIONEND){
                continue;
            }
            CanalEntry.RowChange change = null;
            try {
                change = CanalEntry.RowChange.parseFrom(entry.getStoreValue());
            } catch (Exception e) {
                throw new RuntimeException("ERROR ## parser of eromanga-event has an error , data:" + entry.toString(),e);
            }
            CanalEntry.EventType eventType = change.getEventType();
            String schemeName = entry.getHeader().getSchemaName();
            String tableName = entry.getHeader().getTableName();
            for(CanalEntry.RowData rowData:change.getRowDatasList()){
                if(eventType== CanalEntry.EventType.DELETE){
                    if(schemeName.contains("r_classroom_seat")){

                    }
                    if(tableName.contains("r_classroom_number")){

                    }
                }else {
                    if(schemeName.contains("r_classroom_seat")){
                        classroomSeatHandler.handleColumn(rowData,eventType);
                    }
                    if(tableName.contains("r_classroom_number")){

                    }
                }
            }

        }
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        CanalSubscribe();
    }
}
