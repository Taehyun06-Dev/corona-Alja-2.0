package coronainfo.utils;

import coronainfo.main.Infection_data;
import coronainfo.main.Updater;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Jsutil_global implements Callable{

    private Infection_data dh;

    public Jsutil_global(Infection_data ID){
        dh = ID;
    }

    @Override
    public Boolean call() throws Exception {
        try {
            if (dh.getGlobal_Map() != null) {
                dh.getGlobal_Map().clear();
            }
            Document doc = Jsoup.connect("http://ncov.mohw.go.kr/bdBoardList_Real.do?brddh=&brdGubun=&ncvContSeq=&contSeq=&board_dh=&gubun=").timeout(5000).get();
            Matcher m = Pattern.compile("코로나바이러스감염증-19 환자 총 [0-9]*,[0-9]*명\\(사망 [0-9]*,[0-9]*\\) (.*)").matcher(doc.select("div[class=data_table mgt16] > p").get(1).text());
            if (m.matches()) {
                dh.SetInfection_time_standard_Global(m.group(1));
            }

            Elements elist = doc.select("table.num > tbody > tr > td");
            int[] intlist = {4, 12, 24};
            for(int a : intlist) {
                Matcher m2 = Pattern.compile("([0-9]{1,10})명\\(사망 ([0-9]{1,10})\\)").matcher((elist.get(a+1).text().replaceAll(",", "")));
                if (m2.matches()) {
                    dh.getGlobal_Map().put(elist.get(a).text(), Integer.parseInt(m2.group(1)));
                }
            }
            dh.getGlobal_Map().put("대한민국", dh.getInfection_Checked());
            return true;
        }catch(Exception e) {
            new Updater().notice_info("GlobalMap을 가져오는중 오류가 발생했습니다.", true);
            return false;
        }
    }
}
