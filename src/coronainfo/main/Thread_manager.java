package coronainfo.main;

import coronainfo.utils.Jsutil_global;
import coronainfo.utils.Jsutil_infection;
import coronainfo.utils.Jsutil_region;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Thread_manager {

    private static ExecutorService executorService;
    private static Infection_data ID;

    public void init(Infection_data ID_get){
        executorService = Executors.newFixedThreadPool(3);
        ID = ID_get;
    }

    public void execute(){
        Future<Boolean> future_infection = executorService.submit(new Jsutil_infection(ID));
        Future<Boolean> future_region = executorService.submit(new Jsutil_region(ID));
        Future<Boolean> future_global = executorService.submit(new Jsutil_global(ID));
        try {
            future_infection.get();
            future_region.get();
            future_global.get();
            new Updater().update_all_data(ID);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            new Updater().notice_info("쓰레드풀 에러가 발생하였습니다.", true);
        }
    }

}
