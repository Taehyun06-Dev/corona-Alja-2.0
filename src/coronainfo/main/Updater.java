package coronainfo.main;

import coronainfo.gui.ui_controller;
import javafx.application.Platform;

public class Updater {
	
	private static ui_controller UI;
	private static Infection_data ID;

	public void setController(ui_controller uc) {
		ID = new Infection_data();
		UI = uc;
		new Thread_manager().init(ID);
	}

	public Double get_setting_volume(){ return UI.getSlider_value(); }
	public Boolean get_setting_notice(){ return  UI.getNotice_value(); }
	public void notice_info(String msg, Boolean type) { Platform.runLater(() -> UI.updatelbl_notice(msg, type)); }
	public void update_all_data(Infection_data ID) { Platform.runLater(() -> UI.update_frm_dashboard(ID)); }

}
