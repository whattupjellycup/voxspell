package voxspell.quizSetup;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;

import voxspell.fileManagement.SpellingList;
import voxspell.main.MainMenuPanel;
import voxspell.main.Settings;
import voxspell.quiz.QuizPanel;

public class QuizSetupController {
	private QuizSetupPanel _quizSetupPanel;
	private SpellingList _spellingList = new SpellingList();
	private QuizSetupHandler _quizSetupHandler = new QuizSetupHandler();

	public QuizSetupController(QuizSetupPanel quizSetupPanel){
		_quizSetupPanel = quizSetupPanel;
		//add ActionListeners
		_quizSetupPanel.btnBackToMenu.addActionListener(_quizSetupHandler);
		_quizSetupPanel.btnBegin.addActionListener(_quizSetupHandler);
		_quizSetupPanel.btnRefresh.addActionListener(_quizSetupHandler);
		_quizSetupPanel.chckbxReviewMode.addActionListener(_quizSetupHandler);
		_quizSetupPanel.comboBoxSpellingList.addActionListener(_quizSetupHandler);
		_quizSetupPanel.comboBoxStartCategory.addActionListener(_quizSetupHandler);
		//update spelling lists options
		updateSpellingLists();
	}
	/**
	 * Updates gui elements to match available spelling lists and categories
	 */
	private void updateSpellingLists(){
		_quizSetupPanel.comboBoxSpellingList.removeAllItems();
		for(String list : _spellingList.getLists()){
			//System.out.println(list);
			_quizSetupPanel.comboBoxSpellingList.addItem(list);
		}
	}
	private void updateCategories(String list){
		_quizSetupPanel.comboBoxStartCategory.removeAllItems();
		for(String category: _spellingList.getCategories(list)){
			_quizSetupPanel.comboBoxStartCategory.addItem(category);
		}
		if(_spellingList.getCategories(list).size()==0){
			_quizSetupPanel.comboBoxStartCategory.addItem(SpellingList.NO_CATEGORIES);
		}
	}

	private class QuizSetupHandler implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource()==_quizSetupPanel.btnBackToMenu){
				_quizSetupPanel.vp.show(MainMenuPanel.NAME);
			}
			else if(e.getSource() == _quizSetupPanel.btnBegin){
				_quizSetupPanel.vp.show(QuizPanel.NAME);
			}
			else if(e.getSource()==_quizSetupPanel.btnRefresh){
				updateSpellingLists();
			}
			else if(e.getSource()==_quizSetupPanel.chckbxReviewMode){
				if(_quizSetupPanel.chckbxReviewMode.isSelected()){
					Settings.isReviewMode=true;
					updateCategories(SpellingList.ALL_WORDS); //sneaky way of displaying no categories. All words never has any categories
				}else{
					Settings.isReviewMode=false;
					updateCategories(Settings.currentSpellingList);
				}
			}
			else if(e.getSource()==_quizSetupPanel.comboBoxSpellingList){
				JComboBox<String> cb = (JComboBox<String>)e.getSource();
				String option = (String)cb.getSelectedItem();
				Settings.currentSpellingList=option;
				if(option!=null){
					if(_quizSetupPanel.chckbxReviewMode.isSelected()){
						updateCategories(SpellingList.ALL_WORDS); //sneaky way of displaying no categories. All words never has any categories
					}
					else{
						updateCategories(option);
					}
				}
			}
			else if(e.getSource()==_quizSetupPanel.comboBoxStartCategory){
				JComboBox<String> cb = (JComboBox<String>)e.getSource();
				String option = (String)cb.getSelectedItem();
				Settings.currentCategory=option;
			}
		}
	}
}
