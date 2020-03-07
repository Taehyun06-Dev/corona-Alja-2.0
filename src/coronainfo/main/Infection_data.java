package coronainfo.main;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Infection_data {
	
	private int infection_unchecked;
	private int infection_checked;
	private int infection_isloated;
	private int infection_died;
	private int infection_inspection;
	private String infection_last_update;	
	private String infection_time_standard;
	private String infection_time_standard_region;
	private String infection_time_standard_Global;
	private Map<String, Integer> infection_region_map = new HashMap<>();
	private Map<String, Integer> infection_global_map = new HashMap<>();
	
	public int getInfection_Unchecked() { return infection_unchecked; }
	public void SetInfection_Unchecked(int num) {
		if(num > infection_unchecked){
			try {
				new Notify().notify_sound_and_msg(num-infection_unchecked);
			} catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
				new Updater().notice_info("신규 확진자 알림을 재생할 수 없습니다.", true);
				e.printStackTrace();
			}
		}
		infection_unchecked = num;
	}	
	public int getInfection_Checked() {
		return infection_checked;
	}
	public void SetInfection_Checked(int num) {
		infection_checked = num;
	}	
	public int getInfection_Isloated() {
		return infection_isloated;
	}
	public void SetInfection_Isloated(int num) {
		infection_isloated = num;
	}	
	public int getInfection_Died() {
		return infection_died;
	}
	public void SetInfection_Died(int num) {
		infection_died = num;
	}	
	public int getInfection_Inspection() {
		return infection_inspection;
	}
	public void SetInfection_Inspection(int num) {
		infection_inspection = num;
	}
	
	public String getInfection_last_update() {
		return infection_last_update;
	}
	public void SetInfection_last_update() { infection_last_update = "마지막 업데이트: ".concat(new SimpleDateFormat("HH:mm").format(new Date())); }
	public String getInfection_time_standard() {
		return infection_time_standard;
	}
	public void SetInfection_time_standard(String value) {
		infection_time_standard = value;
	}
	public String getInfection_time_standard_region() {
		return infection_time_standard_region;
	}
	public void SetInfection_time_standard_region(String value) {
		infection_time_standard_region = value;
	}
	public String getInfection_time_standard_Global() {
		return infection_time_standard_Global;
	}
	public void SetInfection_time_standard_Global(String value) {
		infection_time_standard_Global = value;
	}
	
	public Map<String, Integer> getRegion_Map(){
		return infection_region_map;
	}	
	public Map<String, Integer> getGlobal_Map(){
		return infection_global_map;
	}

}
