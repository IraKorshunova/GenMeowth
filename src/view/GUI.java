package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import jm.music.data.Score;
import jm.util.Play;
import jm.util.Read;
import controller.Controller;

@SuppressWarnings("serial")
public class GUI extends JPanel {
    private MidiFilter midiFilter;
    private JButton btn22;
    private JButton btn24;
    private JButton btn34;
    private JButton btn44;
    private JButton btn38;
    private JButton btn68;
    private JButton btn98;
    private JButton btn128;
    private JButton loadBtn;
    private JButton saveBtn;
    private JButton playBtn;
    private JButton generateBtn;
    private JComboBox instrumentBox;
    private JComboBox tempoBox;
    private JComboBox keyQualityBox;
    private JList keySignatureList;
    private Controller controller;
    private ImageIcon[] majorImages;
    private ImageIcon[] minorImages;
    private String[] majorTonalities;
    private String[] minorTonalities;
    private JMenu fileMenu;
    private JFileChooser fileChooser;
    private JFrame frame;
    private boolean generation;
    private JButton prevTimeSigBtn;

    public GUI(JFrame frame) {
	this.frame = frame;
	midiFilter = new MidiFilter();
	controller = new Controller();
	this.setLayout(new BorderLayout());
	this.add(getMenuBar(), BorderLayout.PAGE_START);
	JPanel upperPanel = new JPanel();
	upperPanel.setBorder(new EmptyBorder(new Insets(10, 10, 0, 10)));
	upperPanel.add(getTimeSignaturePanel());
	upperPanel.add(getKeySignaturePanel());
	this.add(upperPanel, BorderLayout.CENTER);
	JPanel lowerPanel = new JPanel();
	lowerPanel.setBorder(new EmptyBorder(new Insets(10, 10, 10, 10)));
	lowerPanel.add(getInstrumentPanel(), BorderLayout.AFTER_LAST_LINE);
	lowerPanel.add(Box.createHorizontalStrut(10));
	lowerPanel.add(getButtonPanel(), BorderLayout.LINE_END);
	this.add(lowerPanel, BorderLayout.PAGE_END);
	fileChooser = new JFileChooser();
	fileChooser.addChoosableFileFilter(midiFilter);
	setListeners();
	generation = true;

    }

    private JMenuBar getMenuBar() {
	JMenuBar menuBar = new JMenuBar();
	fileMenu = new JMenu("File");
	menuBar.add(fileMenu);
	JMenuItem open = fileMenu.add("Open File...");
	JMenuItem save = fileMenu.add("Save As...");
	JMenuItem exit = fileMenu.add("Exit");

	open.addActionListener(new OpenActionListener());
	save.addActionListener(new SaveActionListener());

	exit.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		frame.dispose();
	    }
	});

	JMenu helpMenu = new JMenu("Help");
	menuBar.add(helpMenu);
	JMenuItem about  = helpMenu.add("About");
	about.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		JOptionPane
			.showMessageDialog(
				GUI.this,
				"Ira Korshunova & Sergii Gavrylov",
				"Authors", JOptionPane.INFORMATION_MESSAGE);
	    }
	});
	return menuBar;
    }

    private JPanel getButtonPanel() {
	JPanel buttonPanel = new JPanel();
	buttonPanel.setLayout(new GridLayout(2, 2, 5, 5));

	loadBtn = new JButton("Load");
	buttonPanel.add(loadBtn);

	saveBtn = new JButton("Save");
	buttonPanel.add(saveBtn);

	playBtn = new JButton("Play");
	buttonPanel.add(playBtn);

	generateBtn = new JButton("Generate");
	buttonPanel.add(generateBtn);

	return buttonPanel;
    }

    private JPanel getInstrumentPanel() {
	JPanel panel = new JPanel();
	panel.setLayout(new GridLayout(2, 2, 5, 5));
	panel.add(new JLabel("Select instrument"));
	panel.add(new JLabel("Select tempo"));

	String[] instruments = { "PIANO", "BRIGHT_ACOUSTIC", "ELECTRIC_GRAND",
		"HONKYTONK_PIANO", "ELECTRIC_PIANO", " DX_EPIANO",
		"HARPSICHORD", "CLAVINET", "CELESTA", "GLOCKENSPIEL",
		"MUSIC_BOX", "VIBRAPHONE", "MARIMBA", "XYLOPHONE",
		"TUBULAR_BELL", "ORGAN", "JAZZ_ORGAN", "HAMMOND_ORGAN",
		"CHURCH_ORGAN", "REED_ORGAN", "ACCORDION", "HARMONICA",
		"BANDNEON", "ACOUSTIC_GUITAR", "STEEL_GUITAR", "JAZZ_GUITAR",
		"ELECTRIC_GUITAR", "MUTED_GUITAR", "OVERDRIVE_GUITAR",
		"DISTORTED_GUITAR", "GUITAR_HARMONICS", "ACOUSTIC_BASS",
		"ELECTRIC_BASS", "PICKED_BASS", "FRETLESS_BASS", "SLAP_BASS",
		"SYNTH_BASS", "VIOLIN", "VIOLA", "CELLO", "CONTRABASS",
		"TREMOLO_STRINGS", "PIZZICATO_STRINGS", "HARP", "TIMPANI",
		"STRINGS", "SLOW_STRINGS", "SYNTH_STRINGS", "CHOIR", "VOICE",
		"SYNVOX", "ORCHESTRA_HIT", "TRUMPET", "TROMBONE", "TUBA",
		"MUTED_TRUMPET", "FRENCH_HORN", "BRASS", "SYNTH_BRASS",
		"SOPRANO_SAX", "ALTO_SAX", "TENOR_SAX", "BARITONE_SAX", "OBOE",
		"ENGLISH_HORN", "BASSOON" };

	instrumentBox = new JComboBox(instruments);
	panel.add(instrumentBox);

	String[] tempo = { "GRAVE", "LARGO", "LARGHETTO", "ADAGIO",
		"ADAGIETTO", "ANDANTE", "MODERATO", "ALLEGRO_MODERATO",
		"ALLEGRO", "VIVACE", "PRESTO", "PRESTISSIMO" };
	tempoBox = new JComboBox(tempo);
	panel.add(tempoBox);
	return panel;
    }

    private JPanel getTimeSignaturePanel() {
	JPanel timeSignaturePanel = new JPanel();
	timeSignaturePanel.setBorder(BorderFactory.createCompoundBorder(
		BorderFactory.createTitledBorder("Select a Time Signature"),
		BorderFactory.createEmptyBorder(5, 5, 5, 5)));

	btn22 = new JButton(new ImageIcon("images/22.png"));
	btn24 = new JButton(new ImageIcon("images/24.png"));
	btn34 = new JButton(new ImageIcon("images/34.png"));
	btn44 = new JButton(new ImageIcon("images/44.png"));
	btn38 = new JButton(new ImageIcon("images/38.png"));
	btn68 = new JButton(new ImageIcon("images/68.png"));
	btn98 = new JButton(new ImageIcon("images/98.png"));
	btn128 = new JButton(new ImageIcon("images/128.png"));

	btn22.setMargin(new Insets(0, 0, 0, 0));
	btn24.setMargin(new Insets(0, 0, 0, 0));
	btn34.setMargin(new Insets(0, 0, 0, 0));
	btn44.setMargin(new Insets(0, 0, 0, 0));
	btn38.setMargin(new Insets(0, 0, 0, 0));
	btn68.setMargin(new Insets(0, 0, 0, 0));
	btn98.setMargin(new Insets(0, 0, 0, 0));
	btn128.setMargin(new Insets(0, 0, 0, 0));

	timeSignaturePanel.setLayout(new GridLayout(2, 4));
	timeSignaturePanel.add(btn22);
	timeSignaturePanel.add(btn24);
	timeSignaturePanel.add(btn34);
	timeSignaturePanel.add(btn44);
	timeSignaturePanel.add(btn38);
	timeSignaturePanel.add(btn68);
	timeSignaturePanel.add(btn98);
	timeSignaturePanel.add(btn128);

	prevTimeSigBtn = btn44;
	prevTimeSigBtn.setBackground(Color.LIGHT_GRAY);

	return timeSignaturePanel;
    }

    private JPanel getKeySignaturePanel() {
	JPanel keySignaturePanel = new JPanel();
	keySignaturePanel.setBorder(BorderFactory.createCompoundBorder(
		BorderFactory
			.createTitledBorder("Select a Concert Key Signature"),
		BorderFactory.createEmptyBorder(7, 5, 8, 5)));

	String[] majorValues = { "-10", "-20", "-30", "-40", "-50", "-60",
		"-70", "00", "10", "20", "30", "40", "50", "60", "70" };
	String[] minorValues = { "-11", "-21", "-31", "-41", "-51", "-61",
		"-71", "01", "11", "21", "31", "41", "51", "61", "71" };
	majorTonalities = majorValues;
	minorTonalities = minorValues;

	majorImages = new ImageIcon[15];
	minorImages = new ImageIcon[15];
	for (int i = 0; i < 15; i++) {
	    majorImages[i] = new ImageIcon("images/" + majorTonalities[i]
		    + ".jpg");
	    minorImages[i] = new ImageIcon("images/" + minorTonalities[i]
		    + ".jpg");
	}

	keySignatureList = new JList(majorImages);
	keySignatureList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	keySignatureList.setLayoutOrientation(JList.VERTICAL);
	keySignatureList.setVisibleRowCount(1);
	keySignatureList.setBorder(new EmptyBorder(new Insets(0, 10, 0, 10)));

	JScrollPane scrollPane = new JScrollPane(keySignatureList);
	keySignaturePanel.add(scrollPane);

	String[] scaleName = { "Major key", "Minor key" };
	keyQualityBox = new JComboBox(scaleName);
	keySignaturePanel.add(keyQualityBox);

	return keySignaturePanel;
    }

    private void setListeners() {

	ButtonListener timeSignatureListener = new ButtonListener();
	btn22.addActionListener(timeSignatureListener);
	btn24.addActionListener(timeSignatureListener);
	btn34.addActionListener(timeSignatureListener);
	btn44.addActionListener(timeSignatureListener);
	btn38.addActionListener(timeSignatureListener);
	btn68.addActionListener(timeSignatureListener);
	btn98.addActionListener(timeSignatureListener);
	btn128.addActionListener(timeSignatureListener);

	saveBtn.addActionListener(new SaveActionListener());
	loadBtn.addActionListener(new OpenActionListener());

	generateBtn.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		if (generation) {
		    controller.generateScore();
		} else {
		    controller.createScoreFromFile();
		    keySignatureList.setEnabled(true);
		    generation = true;
		}
	    }
	});

	playBtn.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		Score score = new Score();
		Read.midi(score, "temp.mid");
		Play.midi(score, false);
		JOptionPane.showMessageDialog(frame, "Press ok to stop play",
			"Press ok to stop play", JOptionPane.OK_OPTION);
		Play.stopMidi();
	    }
	});

	keyQualityBox.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		controller.setScale(keyQualityBox.getSelectedIndex());
		keySignatureList.removeAll();
		if (keyQualityBox.getSelectedIndex() == 0) {
		    keySignatureList.setListData(majorImages);
		} else {
		    keySignatureList.setListData(minorImages);
		}
	    }
	});

	keySignatureList.addListSelectionListener(new ListSelectionListener() {

	    @Override
	    public void valueChanged(ListSelectionEvent e) {
		int selectedIndex = keySignatureList.getSelectedIndex();
		if (selectedIndex != -1) {
		    if (keyQualityBox.getSelectedIndex() == 0) {
			controller
				.setKeySignature(majorTonalities[selectedIndex]);
		    } else {
			controller
				.setKeySignature(minorTonalities[selectedIndex]);
		    }
		}

	    }
	});
	instrumentBox.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		try {
		    controller.setInstrument(instrumentBox.getSelectedItem()
			    .toString());
		} catch (IllegalArgumentException e1) {
		} catch (IllegalAccessException e1) {
		}

	    }
	});

	tempoBox.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		try {
		    controller.setTempo(tempoBox.getSelectedItem().toString());
		} catch (IllegalArgumentException e1) {
		} catch (IllegalAccessException e1) {
		}

	    }
	});

    }

    private class ButtonListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
	    JButton btn = (JButton) e.getSource();
	    prevTimeSigBtn.setBackground(new Color(238, 238, 238));
	    prevTimeSigBtn = btn;
	    btn.setBackground(Color.LIGHT_GRAY);
	    controller.setTimeSignature(btn.getIcon().toString());

	}

    }

    private class OpenActionListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
	    boolean ok = false;
	    while (!ok) {
		int returnValue = fileChooser.showOpenDialog(GUI.this);
		if (returnValue == JFileChooser.APPROVE_OPTION) {
		    generation = false;
		    if (midiFilter.getExtension(fileChooser.getSelectedFile())
			    .equals("mid")) {
			controller.setFile(fileChooser.getSelectedFile());
			keySignatureList.setEnabled(false);
			ok = true;
		    } else {
			JOptionPane.showMessageDialog(fileChooser,
				"Incorrect file format!", "ERROR",
				JOptionPane.ERROR_MESSAGE);
		    }
		} else {
		    ok = true;
		}
	    }
	}

    }

    private class SaveActionListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
	    int returnValue = fileChooser.showSaveDialog(GUI.this);
	    if (returnValue == JFileChooser.APPROVE_OPTION) {
		File fromFile = new File("temp.mid");
		File toFile = new File(fileChooser.getSelectedFile().getPath());
		try {
		    FileChannel srcChannel = new FileInputStream(fromFile)
			    .getChannel();
		    FileChannel dstChannel = new FileOutputStream(toFile)
			    .getChannel();
		    dstChannel.transferFrom(srcChannel, 0, srcChannel.size());
		    srcChannel.close();
		    dstChannel.close();
		} catch (IOException ex) {
		    JOptionPane.showMessageDialog(frame,
			    "Can not write to file", "ERROR",
			    JOptionPane.ERROR_MESSAGE);
		}
	    }
	}

    }
}