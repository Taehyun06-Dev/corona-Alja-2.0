package coronainfo.utils;

import coronainfo.main.Infection_data;
import coronainfo.main.Updater;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.concurrent.Callable;

public class Jsutil_region implements Callable {

    private Infection_data dh;

    public Jsutil_region(Infection_data ID) {
        dh = ID;
    }

    @Override
    public Boolean call() throws Exception {
        if (dh.getRegion_Map() != null) {
            dh.getRegion_Map().clear();
        }
        try {
            Document doc = Jsoup.connect("http://ncov.mohw.go.kr/bdBoardList_Real.do?brdId=1&brdGubun=13&ncvContSeq=&contSeq=&board_id=&gubun=").timeout(5000).get();
            for (Element em : doc.select("tbody > tr")) {
                if (em.select("th").get(0).text().equals("합계")) {
                    dh.SetInfection_Checked(Integer.parseInt(em.select("td").get(1).text().replaceAll(",", "")));
                } else {
                    dh.getRegion_Map().put(em.select("th").get(0).text(), Integer.parseInt(em.select("td").get(1).text().replaceAll(",", "")));
                }
            }
            dh.SetInfection_time_standard_region(doc.select("div.timetable > p").get(0).text());
            dh.SetInfection_last_update();
            return true;
        } catch (Exception e) {
            new Updater().notice_info("RegionMap을 가져오는중 오류가 발생했습니다.", true);
            return false;
        }
    }
}