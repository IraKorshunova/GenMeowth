package controller;

import generator.AntHill;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;

import jm.constants.Pitches;
import jm.music.data.Score;
import jm.util.Read;
import jm.util.Write;
import music.constants.Duration;
import music.constants.Instrument;
import music.constants.Tempo;
import music.util.Harmonizator;
import musicLevak.Note;

public class Controller {
    int keyQuality;
    int keySignature;
    int numerator;
    int denominator;
    int tempo;
    int instrument;
    File file;

    public Controller() {
	keyQuality = 0;
	keySignature = 0;
	numerator = 4;
	denominator = 4;
	tempo = Tempo.GRAVE;
	instrument = Instrument.PIANO;
    }

    public void setScale(int keyQuality) {
	this.keyQuality = keyQuality;
    }

    public void setKeySignature(String keyValue) {
	if (keyValue.length() == 3) {
	    keySignature = -1
		    * (Integer.valueOf(keyValue.charAt(1)).intValue() - 48);
	} else {
	    keySignature = Integer.valueOf(keyValue.charAt(0)).intValue() - 48;
	}
    }

    public void setInstrument(String instrument)
	    throws IllegalArgumentException, IllegalAccessException {
	Instrument instrumentObj = new Instrument();
	Field[] fields = instrumentObj.getClass().getFields();
	for (Field field : fields) {
	    if (field.getName() == instrument) {
		this.instrument = field.getInt(instrumentObj);
		break;
	    }

	}
    }

    public void setTempo(String tempo) throws IllegalArgumentException,
	    IllegalAccessException {
	Tempo tempoObj = new Tempo();
	Field[] fields = tempoObj.getClass().getFields();
	for (Field field : fields) {
	    if (field.getName() == tempo) {
		this.tempo = field.getInt(tempoObj);
		break;
	    }

	}

    }

    public void setTimeSignature(String timeSignature) {
	if (timeSignature.length() == 13) {
	    numerator = Integer.valueOf(timeSignature.charAt(7)).intValue() - 48;
	    denominator = Integer.valueOf(timeSignature.charAt(8)).intValue() - 48;
	} else {
	    numerator = Integer.valueOf(timeSignature.substring(7, 9))
		    .intValue();
	    denominator = Integer.valueOf(timeSignature.charAt(9)).intValue() - 48;
	}
    }

    public void createScoreFromFile() {
	Score scr = new Score();
	Read.midi(scr, file.getPath());
	keySignature = scr.getKeySignature();
	jm.music.data.Note[] jmNotes = scr.getPart(0).getPhrase(0)
		.getNoteArray();
	Note[] notes = new Note[jmNotes.length];
	int j = 0;
	for (jm.music.data.Note note : jmNotes) {
	    notes[j] = new Note(note);
	    j++;
	}
	ArrayList<Note> melody = new ArrayList<Note>();
	for (int i = 0; i < notes.length; i++) {
	    if (notes[i].getPitch() >= 0) {
		melody.add(notes[i]);
	    }
	}

	AntHill<Note> antHill = new AntHill<Note>(melody, 10,
		AntHill.PATERNS_BUILDING);
	antHill.makeStep(100);
	melody = antHill.generateObjectsArray(80);
	harmonize(melody);
    }

    public void setFile(File file) {
	this.file = file;
    }

    public void generateScore() {
	ArrayList<Note> list = new ArrayList<Note>();

	list.add(new Note(Pitches.B4, Duration.EIGHTH_NOTE));
	list.add(new Note(Pitches.A4, Duration.EIGHTH_NOTE));
	list.add(new Note(Pitches.G4, Duration.EIGHTH_NOTE));
	list.add(new Note(Pitches.F4, Duration.EIGHTH_NOTE));
	list.add(new Note(Pitches.E4, Duration.EIGHTH_NOTE));
	list.add(new Note(Pitches.D4, Duration.EIGHTH_NOTE));
	list.add(new Note(Pitches.C4, Duration.EIGHTH_NOTE));
	list.add(new Note(Pitches.B3, Duration.EIGHTH_NOTE));
	list.add(new Note(Pitches.A3, Duration.EIGHTH_NOTE));
	list.add(new Note(Pitches.G3, Duration.EIGHTH_NOTE));
	list.add(new Note(Pitches.F3, Duration.EIGHTH_NOTE));
	list.add(new Note(Pitches.E3, Duration.EIGHTH_NOTE));
	list.add(new Note(Pitches.D3, Duration.EIGHTH_NOTE));
	list.add(new Note(Pitches.C3, Duration.EIGHTH_NOTE));
	list.add(new Note(Pitches.B2, Duration.EIGHTH_NOTE));
	list.add(new Note(Pitches.A2, Duration.EIGHTH_NOTE));
	list.add(new Note(Pitches.G2, Duration.EIGHTH_NOTE));
	list.add(new Note(Pitches.F2, Duration.EIGHTH_NOTE));
	list.add(new Note(Pitches.E2, Duration.EIGHTH_NOTE));
	list.add(new Note(Pitches.D2, Duration.EIGHTH_NOTE));
	list.add(new Note(Pitches.C2, Duration.EIGHTH_NOTE));

	list.add(new Note(Pitches.B4, Duration.QUARTER_NOTE_TRIPLET));
	list.add(new Note(Pitches.A4, Duration.QUARTER_NOTE_TRIPLET));
	list.add(new Note(Pitches.G4, Duration.QUARTER_NOTE_TRIPLET));
	list.add(new Note(Pitches.F4, Duration.QUARTER_NOTE_TRIPLET));
	list.add(new Note(Pitches.E4, Duration.QUARTER_NOTE_TRIPLET));
	list.add(new Note(Pitches.D4, Duration.QUARTER_NOTE_TRIPLET));
	list.add(new Note(Pitches.C4, Duration.QUARTER_NOTE_TRIPLET));
	list.add(new Note(Pitches.B3, Duration.QUARTER_NOTE_TRIPLET));
	list.add(new Note(Pitches.A3, Duration.QUARTER_NOTE_TRIPLET));
	list.add(new Note(Pitches.G3, Duration.QUARTER_NOTE_TRIPLET));
	list.add(new Note(Pitches.F3, Duration.QUARTER_NOTE_TRIPLET));
	list.add(new Note(Pitches.E3, Duration.QUARTER_NOTE_TRIPLET));
	list.add(new Note(Pitches.D3, Duration.QUARTER_NOTE_TRIPLET));
	list.add(new Note(Pitches.C3, Duration.QUARTER_NOTE_TRIPLET));
	list.add(new Note(Pitches.B2, Duration.QUARTER_NOTE_TRIPLET));
	list.add(new Note(Pitches.A2, Duration.QUARTER_NOTE_TRIPLET));
	list.add(new Note(Pitches.G2, Duration.QUARTER_NOTE_TRIPLET));
	list.add(new Note(Pitches.F2, Duration.QUARTER_NOTE_TRIPLET));
	list.add(new Note(Pitches.E2, Duration.QUARTER_NOTE_TRIPLET));
	list.add(new Note(Pitches.D2, Duration.QUARTER_NOTE_TRIPLET));
	list.add(new Note(Pitches.C2, Duration.QUARTER_NOTE_TRIPLET));

	list.add(new Note(Pitches.B4, Duration.QUARTER_NOTE));
	list.add(new Note(Pitches.A4, Duration.QUARTER_NOTE));
	list.add(new Note(Pitches.G4, Duration.QUARTER_NOTE));
	list.add(new Note(Pitches.F4, Duration.QUARTER_NOTE));
	list.add(new Note(Pitches.E4, Duration.QUARTER_NOTE));
	list.add(new Note(Pitches.D4, Duration.QUARTER_NOTE));
	list.add(new Note(Pitches.C4, Duration.QUARTER_NOTE));
	list.add(new Note(Pitches.B3, Duration.QUARTER_NOTE));
	list.add(new Note(Pitches.A3, Duration.QUARTER_NOTE));
	list.add(new Note(Pitches.G3, Duration.QUARTER_NOTE));
	list.add(new Note(Pitches.F3, Duration.QUARTER_NOTE));
	list.add(new Note(Pitches.E3, Duration.QUARTER_NOTE));
	list.add(new Note(Pitches.D3, Duration.QUARTER_NOTE));
	list.add(new Note(Pitches.C3, Duration.QUARTER_NOTE));
	list.add(new Note(Pitches.B2, Duration.QUARTER_NOTE));
	list.add(new Note(Pitches.A2, Duration.QUARTER_NOTE));
	list.add(new Note(Pitches.G2, Duration.QUARTER_NOTE));
	list.add(new Note(Pitches.F2, Duration.QUARTER_NOTE));
	list.add(new Note(Pitches.E2, Duration.QUARTER_NOTE));
	list.add(new Note(Pitches.D2, Duration.QUARTER_NOTE));
	list.add(new Note(Pitches.C2, Duration.QUARTER_NOTE));

	AntHill<Note> antHill = new AntHill<Note>(list, 10,
		AntHill.NATURALS_BUILDING);
	antHill.makeStep(100);
	antHill.setGeneratingAntPosition(antHill.getRandomNode());
	ArrayList<Note> melody = antHill.generateObjectsArray(50);
	Harmonizator.fitToKey(melody, keySignature);
	harmonize(melody);

    }

    private void harmonize(ArrayList<Note> melody) {
	int tonic;
	if (keyQuality == 0) {
	    tonic = Harmonizator.getKeys(keySignature)[0];
	} else {
	    tonic = Harmonizator.getKeys(keySignature)[1];
	}
	Harmonizator harmonizator = new Harmonizator(melody, numerator,
		denominator, keyQuality, tonic);
	Score score = harmonizator.createScore();
//	score.getPart(0).setInstrument(instrument);
	score.getPart(0).getPhrase(1).setInstrument(instrument);
//	score.getPart(0).getPhrase(0).setInstrument(Instrument.PIANO);
	score.setTempo(tempo);
	Write.midi(score, "temp.mid");
    }
}