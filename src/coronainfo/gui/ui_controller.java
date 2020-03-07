package coronainfo.gui;


import coronainfo.main.Infection_data;
import coronainfo.main.Thread_manager;
import coronainfo.main.Updater;
import javafx.animation.KeyFrame;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.util.Duration;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ui_controller implements Initializable {
	//FXML value
    @FXML
    private PieChart pieHash, pieKey;
    @FXML
    private Pane frm_dashboard, frm_map, frm_notify, frm_setting, frm_stat;
    @FXML
    private Button btt_dashboard, btt_map, btt_stat, btt_notice, btt_setting, btt_delnotice;
    @FXML
    private WebView webview_panel, web_news;
    @FXML
	private WebEngine we, we_news;
    @FXML
    private ImageView img_update, img_loader, img_notice_alrt, img_gotosite, img_gotoweb, img_notset, img_load_news;
    @FXML
    private Label lbl_getslider, lbl_standard_global, lbl_info, lbl_warn, lbl_infected, lbl_isolated, lbl_died, lbl_inspection, lbl_lastupdate, lbl_showinfectionnum, lbl_standard_infection, lbl_standard_region;
    @FXML
    private BarChart<String, Integer> chart_region, chart_global;
    @FXML
	private ChoiceBox<Integer> spinner_updatetime;
    @FXML
	private CheckBox cbox_updatenotice, cbox_addtionalinfection;
    @FXML
	private Slider slider_volume;
	//Normal value
    private Double get_slider_volunme;
    private Timeline timeline;
    private Updater up;
    private Boolean enlarged;


    //Getter
    private String getComma_num(int input) {
    	return String.format("%,d", input);
    }
    public Double getSlider_value(){ return get_slider_volunme; }
    public Boolean getNotice_value(){ return cbox_addtionalinfection.isSelected(); }
    
    //Ui Updater
    public void updatelbl_notice(String msg, Boolean type) {
		String current_time = (new SimpleDateFormat("HH:mm:ss").format(new Date()));
		img_notice_alrt.setVisible(true);
		int i = 0;
		if (type) {
			lbl_warn.setText(lbl_warn.getText() + "[" + current_time + " WARN]: " + msg + "\n");
			Matcher m = Pattern.compile("\n").matcher(lbl_warn.getText());
			while (m.find()) {
				i++;
				if (i > 30) {
					lbl_warn.setText("[" + current_time + " WARN]: 자동으로 모든 알림울 삭제했습니다.\n");
					break;
				}
			}
		} else {
			lbl_info.setText(lbl_info.getText() + "[" + current_time + " INFO]: " + msg + "\n");
			Matcher m = Pattern.compile("\n").matcher(lbl_info.getText());
			while (m.find()) {
				i++;
				if (i > 30) {
					lbl_info.setText("[" + current_time + " INFO]: 자동으로 모든 알림를 삭제했습니다.\n");
					break;
				}
			}
		}
    	
    }
    @SuppressWarnings("unchecked")
	public void updateChart_data(Infection_data ID) {
    	ObservableList<Data> data = FXCollections.observableArrayList();
    	for (Entry<String, Integer> e : ID.getRegion_Map().entrySet()) {
    		String k = String.valueOf(e.getKey());
    		Integer v = e.getValue();               
    		data.add(new Data(k, v));
    	}
    	pieHash.setData(data);
        ObservableList<Data> data2 = FXCollections.observableArrayList();
    	for (Entry<String, Integer> e : ID.getGlobal_Map().entrySet()) {
    		String k = String.valueOf(e.getKey());
    		Integer v = e.getValue();	
    		data2.add(new Data(k, v));
    	}
    	pieKey.setData(data2); 
    	XYChart.Series<String, Integer> series1 = new XYChart.Series<>();
    	for (Entry<String, Integer> entry : ID.getRegion_Map().entrySet()) {
    		series1.getData().add(new XYChart.Data<>(entry.getKey()+"\n("+entry.getValue()+")", entry.getValue()));
    	}   	   	
    	chart_region.setAnimated(false);
    	chart_region.getData().clear();
    	chart_region.getData().addAll(series1);	
    	XYChart.Series<String, Integer> series2 = new XYChart.Series<>();
    	for (Entry<String, Integer> entry2 : ID.getGlobal_Map().entrySet()) {
    		series2.getData().add(new XYChart.Data<>(entry2.getKey()+"("+entry2.getValue()+")", entry2.getValue()));
    	}   	   	
    	chart_global.setAnimated(false);
    	chart_global.getData().clear();
    	chart_global.getData().addAll(series2);
    }

    public void update_frm_dashboard(Infection_data ID) {
         lbl_infected.setText(getComma_num(ID.getInfection_Unchecked()));
         lbl_isolated.setText(getComma_num(ID.getInfection_Isloated()));
         lbl_died.setText(getComma_num(ID.getInfection_Died()));
         lbl_inspection.setText(getComma_num(ID.getInfection_Inspection()));
         lbl_lastupdate.setText(ID.getInfection_last_update());
         lbl_showinfectionnum.setText("총 확진자 수: "+ID.getInfection_Checked()+"명(동선확인) / "+ID.getInfection_Unchecked()+"명");
         lbl_standard_infection.setText(ID.getInfection_time_standard());
         lbl_standard_global.setText(ID.getInfection_time_standard_Global());
         lbl_standard_region.setText("("+ID.getInfection_time_standard_region()+")");
         updateChart_data(ID);
         up.notice_info("모든 정보를 업데이트 했습니다.", false);
    }

    private void update_all_data(){
    	new Thread_manager().execute();
		we_news.load("https://news.naver.com/main/list.nhn?mode=LS2D&mid=sec&sid1=103&sid2=241");
		RotateTransition rt = new RotateTransition(Duration.millis(2000), img_update);
		rt.setByAngle(180);
		rt.setCycleCount(1);
		rt.play();
	}

    private void update_auto_timer(int i){
    	if(timeline != null){
			updatelbl_notice("타이머 주기가 새롭게 설정되었습니다.", false);
    		timeline.stop();
		}
    	timeline = new Timeline(new KeyFrame(Duration.minutes(i), event -> {
			update_all_data();
		}));
		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.play();
	}

	//Setup
    private void setup_addListener(){
		btt_setting.setOnMouseClicked(e -> frm_setting.toFront());
		btt_notice.setOnMouseClicked(e -> {
			frm_notify.toFront();
			img_notice_alrt.setVisible(false);
		});
		btt_stat.setOnMouseClicked(e -> frm_stat.toFront());
		img_gotosite.setOnMouseClicked(e -> {
			if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
				try {
					Desktop.getDesktop().browse(new URI("http://ncov.mohw.go.kr/index_main.jsp"));
				} catch (IOException | URISyntaxException e1) {
					updatelbl_notice("브라우저를 열 수 없습니다.", true);
				}
			}
		});
		btt_map.setOnMouseClicked(e -> {
			frm_map.toFront();
			img_loader.setVisible(true);
			we.load("http://coronamap.site/");
			we.getLoadWorker().stateProperty().addListener(
					(ov, oldState, newState) -> { if (newState == Worker.State.SUCCEEDED) { img_loader.setVisible(false); } });
		});
		btt_dashboard.setOnMouseClicked(e -> frm_dashboard.toFront());
		//Image Event
		img_update.setOnMouseClicked(e-> update_all_data());
		btt_delnotice.setOnMouseClicked(e -> {
			lbl_info.setText("");
			lbl_warn.setText("");
		});
		slider_volume.valueProperty().addListener(e -> {
			lbl_getslider.setText("(설정값: "+String.format("%.2f", slider_volume.getValue())+")");
			get_slider_volunme = slider_volume.getValue();
		});
		spinner_updatetime.valueProperty().addListener(e -> update_auto_timer(spinner_updatetime.getValue()));
	}

	private void setup_Setting(){
		int[] list = {1, 2, 3, 5, 7, 10, 15, 30, 50};
		for(int i : list) {
			spinner_updatetime.getItems().add(i);
		}
		spinner_updatetime.setValue(3);
		cbox_addtionalinfection.setSelected(true);
		cbox_updatenotice.setSelected(true);
		cbox_updatenotice.setDisable(true);
		slider_volume.setMin(0);
		slider_volume.setMax(100);
		slider_volume.setValue(50);
		slider_volume.setShowTickLabels(true);
		slider_volume.setShowTickMarks(true);
		slider_volume.setSnapToTicks(true);
		slider_volume.setBlockIncrement(20);
		update_auto_timer(3);
	}
	private void setup_Newspanel(){
		we_news.load("https://news.naver.com/main/list.nhn?mode=LS2D&mid=sec&sid1=103&sid2=241");
		enlarged = null;
		img_load_news.setVisible(true);
		we_news.getLoadWorker().stateProperty().addListener(
				(ov, oldState, newState) -> { if (newState == Worker.State.SUCCEEDED) { img_load_news.setVisible(false); enlarged = false;
				} });
		img_notset.setImage(new Image(getClass().getResourceAsStream("/coronainfo/resources/enlarge_24px.png")));
		img_notset.setOnMouseClicked(e -> {
			if(enlarged == null){
				return;
			}
			if(enlarged){
				img_notset.setImage(new Image(getClass().getResourceAsStream("/coronainfo/resources/enlarge_24px.png")));
				web_news.setLayoutX(414);
				web_news.setLayoutY(14);
				web_news.setPrefWidth(347);
				web_news.setPrefHeight(241);
				enlarged = false;
			}else {
				img_notset.setImage(new Image(getClass().getResourceAsStream("/coronainfo/resources/collapse_24px.png")));
				web_news.setLayoutX(0);
				web_news.setLayoutY(0);
				web_news.setPrefWidth(765);
				web_news.setPrefHeight(530);
				enlarged = true;
			}
		});
		img_gotoweb.setOnMouseClicked(e -> {
			if(enlarged == null){
				return;
			}
			if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
				try {
					Desktop.getDesktop().browse(new URI("https://news.naver.com/main/list.nhn?mode=LS2D&mid=sec&sid1=103&sid2=241"));
				} catch (IOException | URISyntaxException e1) {
					updatelbl_notice("브라우저를 열 수 없습니다.", true);
				}
			}
		});
	}
	//Main
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	up = new Updater();
    	up.setController(this);
		img_notice_alrt.setVisible(false);
		we = webview_panel.getEngine();
		we_news = web_news.getEngine();
		setup_addListener();
		setup_Setting();
		setup_Newspanel();
    }
}