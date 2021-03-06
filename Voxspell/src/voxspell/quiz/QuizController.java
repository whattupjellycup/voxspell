package voxspell.quiz;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;

import voxspell.fileManagement.SpellingList;
import voxspell.fileManagement.Statistics;
import voxspell.main.Controller;
import voxspell.main.MainMenuPanel;
import voxspell.main.Settings;
import voxspell.media.audio.Music;
import voxspell.media.audio.SoundEffect;
import voxspell.media.audio.voice.Festival;
import voxspell.media.video.VideoReward;

public class QuizController implements Controller{
	private QuizPanel _quizPanel;
	private Quiz _quizModel;
	private QuizHandler _quizHandler = new QuizHandler();
	private MouseHover _mouseHover = new MouseHover();
	private SoundEffect _soundEffect = new SoundEffect();
	private Statistics _stats = new Statistics();
	
	public QuizController(QuizPanel quizPanel, Quiz quizModel){
		_quizPanel = quizPanel;
		_quizModel = quizModel;

		//add actionListeners
		_quizPanel.inputTextField.addActionListener(_quizHandler);
		_quizPanel.btnHearWord.addActionListener(_quizHandler);
		_quizPanel.btnEndQuiz.addActionListener(_quizHandler);
		_quizPanel.btnHearWord.addActionListener(_quizHandler);
		_quizPanel.btnSpellWord.addActionListener(_quizHandler);
		_quizPanel.btnStart.addActionListener(_quizHandler);
		_quizPanel.btnAudioReward.addActionListener(_quizHandler);
		_quizPanel.btnVideoReward.addActionListener(_quizHandler);
		//add mouselisteners
		_quizPanel.btnHearWord.addMouseListener(_mouseHover);
		_quizPanel.btnEndQuiz.addMouseListener(_mouseHover);
		_quizPanel.btnHearWord.addMouseListener(_mouseHover);
		_quizPanel.btnSpellWord.addMouseListener(_mouseHover);
		_quizPanel.btnStart.addMouseListener(_mouseHover);
		_quizPanel.btnAudioReward.addMouseListener(_mouseHover);
		_quizPanel.btnVideoReward.addMouseListener(_mouseHover);
	}
	
	/**
	 * Updates the progress in the UI
	 */
	private void updateProgress(){
		_quizPanel.lblProgress.setText("Spelling word "+_quizModel.getWordNumber()+" out of "+_quizModel.getQuizLength());
	}
	
	/**
	 * update the end of quiz panel
	 */
	private void updateEndPanel(){
		if((double)_quizModel.getNumberOfCorrect()/(double)_quizModel.getQuizLength()>0.8){
			_quizPanel.lblCongratulations.setVisible(true);
			_quizPanel.btnAudioReward.setVisible(true);
			_quizPanel.btnVideoReward.setVisible(true);
		}else{
			_quizPanel.lblCongratulations.setVisible(false);
			_quizPanel.btnAudioReward.setVisible(false);
			_quizPanel.btnVideoReward.setVisible(false);
		}
		_quizPanel.lblCategoryaccuracy.setText(_stats.getRecentAccuracy(Settings.currentSpellingList, Settings.currentCategory)+"%");
		_quizPanel.lblListaccuracy.setText(_stats.getRecentAccuracy(Settings.currentSpellingList, SpellingList.ALL_CATEGORIES)+"%");
		_quizPanel.lblYouGotnumber.setText("You got "+_quizModel.getNumberOfCorrect()+" out of "+_quizModel.getQuizLength());
		_quizPanel.lblYourlistAccuracy.setText("Your "+Settings.currentSpellingList+" accuracy is now:");
		_quizPanel.lblYourcategoryAccuracy.setText("Your "+Settings.currentCategory+" accuracy is now:");
	}
	
	/**
	 * Actionlistener for quiz
	 * @author Gillon Manalastas
	 *
	 */
	private class QuizHandler implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource()==_quizPanel.inputTextField){
				if(!_quizModel.isQuizEnded()){
					_quizModel.quizAttempt(_quizPanel.inputTextField.getText());
					if(_quizModel.isNextWord()){
						_quizPanel.lblWordSpelling.setText("");
						_quizModel.setUpdateble(true);
					}
				}
				updateProgress();
				_quizPanel.inputTextField.setText("");
				if(_quizModel.isQuizEnded()){
					updateEndPanel();
					_quizPanel.inputPanel.setVisible(false);
					_quizPanel.endPanel.setVisible(true);
				}
			}
			else if(e.getSource()==_quizPanel.btnEndQuiz){
				_soundEffect.playClick();
				_quizModel.isQuizEnded(true);
				_quizPanel.vp.show(MainMenuPanel.NAME);
			}
			else if(e.getSource()==_quizPanel.btnHearWord){
				_soundEffect.playClick();
				if(!_quizModel.isQuizEnded()){
					_quizModel.sayWord();
				}
			}
			else if(e.getSource()==_quizPanel.btnStart){
				_soundEffect.playClick();
				_quizPanel.inputPanel.setVisible(true);
				_quizPanel.btnStart.setVisible(false);
				_quizModel.startQuiz();
				updateProgress();
			}
			else if(e.getSource()==_quizPanel.btnSpellWord){
				_soundEffect.playClick();
				if(!_quizModel.isQuizEnded()){
					_quizModel.setUpdateble(false);
					_quizModel.spellWord();
					_quizPanel.lblWordSpelling.setText(_quizModel.getWord());
				}
			}
			else if(e.getSource()==_quizPanel.btnVideoReward){
				_soundEffect.playClick();
				_quizPanel.vp.show(VideoReward.NAME);
			}
			else if(e.getSource()==_quizPanel.btnAudioReward){
				_soundEffect.playClick();
				_soundEffect.playCheer();
			}
		}
	}
	
	private class MouseHover extends MouseAdapter{
		@Override
		public void mouseEntered(MouseEvent e) {
			((JComponent) e.getSource()).setBackground(Color.BLACK);
			((JComponent) e.getSource()).setForeground(Color.WHITE);
		}
		@Override
		public void mouseExited(MouseEvent e) {
			((JComponent) e.getSource()).setForeground(Color.BLACK);
			((JComponent) e.getSource()).setBackground(Color.WHITE);
		}
	}

	@Override
	public void refresh() {
		if(Settings.isReviewMode){
			_quizPanel.btnSpellWord.setVisible(true);
			_quizPanel.lblWordSpelling.setVisible(true);
			_quizPanel.lblSpellingListQuiz.setText(Settings.currentSpellingList+" Review Quiz");
		}else{
			_quizPanel.lblWordSpelling.setVisible(false);
			_quizPanel.btnSpellWord.setVisible(false);
			_quizPanel.lblSpellingListQuiz.setText(Settings.currentSpellingList+" Quiz");
		}
		if(Settings.currentCategory.equals(SpellingList.NO_CATEGORIES)){
			_quizPanel.lblcategory.setText("");
		}else{
			_quizPanel.lblcategory.setText(Settings.currentCategory);
		}
		_quizPanel.inputPanel.setVisible(false);
		_quizPanel.endPanel.setVisible(false);
		_quizPanel.btnStart.setVisible(true);
		_quizPanel.inputTextField.setText("");
		_quizPanel.lblWordSpelling.setText("");
	}
}
