package coronainfo.utils;

import coronainfo.main.Infection_data;
import coronainfo.main.Updater;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Jsutil_infection implements Callable {

    private Infection_data dh;

    public Jsutil_infection(Infection_data ID){
        dh = ID;
    }

    @Override
    public Boolean call() throws Exception {
        try {
            Document doc = Jsoup.connect("http://ncov.mohw.go.kr/bdBoardList_Real.do?brdId=&brdGubun=&ncvContSeq=&contSeq=&board_id=&gubun=").timeout(5000).get();
            Matcher m = Pattern.compile("코로나바이러스감염증-19 국내 발생 현황(.+)").matcher(doc.select("div.bvc_txt > p").get(0).text());
            if (m.matches()) {
                dh.SetInfection_time_standard(m.group(1));
            }
            boolean infection_set = false;
            for (Element em : doc.select("table.num > tbody > tr")) {
                if (em.text().contains("확진") || em.text().contains("사망자") || em.text().contains("검사")) {
                    String[] list2 = em.text().split(" ");
                    switch (list2[0]) {
                        case "확진환자":
                            if (!infection_set) {
                                dh.SetInfection_Unchecked(Integer.parseInt(list2[1].replaceAll(",", "")));
                                infection_set = true;
                                continue;
                            }
                            dh.SetInfection_Isloated(Integer.parseInt(list2[2].replaceAll(",", "")));
                            continue;
                        case "사망자":
                            dh.SetInfection_Died(Integer.parseInt(list2[1].replaceAll(",", "")));
                            continue;
                        case "검사진행":
                            dh.SetInfection_Inspection(Integer.parseInt(list2[1].replaceAll(",", "")));
                            return true;
                        default:
                    }
                }
            }
        } catch (Exception e) {
            new Updater().notice_info("InfectionMap을 가져오는중 오류가 발생했습니다.", true);
            e.printStackTrace();
            return false;
        }
        return false;
    }
}
