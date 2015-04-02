package music.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import jm.music.data.Part;
import jm.music.data.Phrase;
import jm.music.data.Score;
import music.constants.Volume;
import music.exception.IllegalPieceAttributesException;
import musicLevak.Note;

/**
 * The class <code>Harmonizator</code> contains methods for making a chordal
 * accompaniment to a melody.
 */

public class Harmonizator {
    private ArrayList<Note> melody;
    private ArrayList<ArrayList<Note>> bars;
    private Phrase backing;
    private double chordDuration;
    private int octave;
    private int notesNum;
    private int numerator;
    private int denominator;
    private int keyQuality;
    private int tonic;
    private int dominant;
    private int subdominant;
    private int[] dominantGroup;
    private int[] subdominantGroup;
    private int[][] transMatrix;

    public Harmonizator(ArrayList<Note> melody, int numerator, int denominator,
	    int keyQuality, int tonic) {
	this.numerator = numerator;
	this.denominator = denominator;
	this.keyQuality = keyQuality;
	this.tonic = tonic;
	this.notesNum = melody.size();
	this.melody = melody;
	this.backing = new Phrase("backing");

	dominant = (tonic + 7) % 12;
	subdominant = (tonic + 5) % 12;

	dominantGroup = new int[3];
	subdominantGroup = new int[3];
	if (keyQuality == 1) {
	    dominantGroup[0] = (tonic + 3) % 12; // III
	    subdominantGroup[2] = (tonic + 10) % 12; // VI
	} else {
	    dominantGroup[0] = (tonic + 4) % 12; // III
	    subdominantGroup[2] = (tonic + 9) % 12; // VI
	}
	dominantGroup[1] = dominant; // V
	dominantGroup[2] = (tonic + 11) % 12; // VII
	subdominantGroup[0] = (tonic + 2) % 12; // II
	subdominantGroup[1] = subdominant; // IV
	if (keyQuality == 1)
	    melodyHarmonicMinor();
	divideIntoBars();
	initializeTransitMatrix();
	fitBackingOctave();
	fitHarmony();

    }

    private void melodyHarmonicMinor() {
	Note checkedNote;
	for (int i = 0; i < melody.size(); i++) {
	    checkedNote = melody.get(i);
	    if (checkedNote.getPitch() % 12 == dominantGroup[2] - 1) {
		melody.set(
			i,
			new Note(checkedNote.getPitch() + 1, checkedNote
				.getRhythmValue()));
	    }
	}
    }

    /**
     * Divides a melody into half bars up to semi quaver triplet note.
     * */
    private void divideIntoBars() {
	final double EPSILON = 0.2;
	double barLength = numerator / denominator * 4.0;
	double sumDuration = 0.0;
	double buffDuration = 0.0;
	double carryDuration = 0.0;
	boolean carryFlag = false;
	Note buffNote = new Note();
	bars = new ArrayList<ArrayList<Note>>();

	switch (denominator) {
	case 2: {
	    chordDuration = barLength = 2;
	    break;
	}
	case 4: {
	    if (numerator == 3)
		chordDuration = barLength = 3;
	    else {
		chordDuration = barLength = 2;
	    }
	    break;
	}
	case 8: {
	    chordDuration = barLength = 1.5;
	    break;
	}
	}

	int barsNumber = getBarsNumber(barLength);
	int j = -1;
	for (int i = 0; i < barsNumber; i++) {
	    ArrayList<Note> bar = new ArrayList<Note>();
	    bars.add(bar);

	    sumDuration = 0.0;
	    if (carryFlag && buffNote.getRhythmValue() > EPSILON) {
		buffDuration = buffNote.getRhythmValue();
		sumDuration += buffDuration;
		if (sumDuration > barLength + EPSILON) {
		    carryFlag = true;
		    sumDuration -= buffDuration;
		    carryDuration = buffDuration;
		    buffDuration = barLength - sumDuration;
		    carryDuration -= buffDuration;
		    if (buffDuration > EPSILON)
			bar.add(new Note(melody.get(j).getPitch(), buffDuration));
		    buffNote = new Note(melody.get(j).getPitch(), carryDuration);
		} else {
		    carryFlag = false;
		    if (buffDuration > EPSILON)
			bar.add(buffNote);
		}

	    }

	    while (!carryFlag && j < notesNum - 1) {
		j++;
		buffNote = melody.get(j);
		buffDuration = melody.get(j).getRhythmValue();
		sumDuration += buffDuration;
		if ((sumDuration < barLength + EPSILON)
			&& (buffDuration > EPSILON)) {
		    bar.add(buffNote);
		    if (sumDuration < barLength + EPSILON
			    && sumDuration > barLength - EPSILON) {
			break;
		    }
		} else if (buffDuration > EPSILON) {
		    carryFlag = true;
		    sumDuration -= buffDuration;
		    carryDuration = buffDuration;
		    buffDuration = barLength - sumDuration;
		    carryDuration -= buffDuration;
		    if (buffDuration > EPSILON)
			bar.add(new Note(melody.get(j).getPitch(), buffDuration));
		    buffNote = new Note(melody.get(j).getPitch(), carryDuration);
		}
	    }
	}
    }

    /**
     * Returns the approximated number of bars
     * 
     * @param barLength the length of bar.
     * @return number of bars in the score.
     * */
    private int getBarsNumber(double barLength) {
	double sumDuration = 0;
	for (int i = 0; i < notesNum; i++) {
	    sumDuration += melody.get(i).getRhythmValue();
	}
	return (int) Math.round(sumDuration / barLength);
    }

    /**
     * Returns the pitch of the longest note in the bar in the contra octave
     * (numeric values 0-11)
     * 
     * @param noteList notes in the bar.
     * @return longest note in the bar.
     * */
    private int barHarmony(ArrayList<Note> noteList) {
	ArrayList<Note> noRepsNoteList = new ArrayList<Note>();
	int curPitch = 0;
	double sumDuration = 0;

	for (int i = 0; i < noteList.size(); i++) {
	    sumDuration = noteList.get(i).getRhythmValue();
	    curPitch = noteList.get(i).getPitch();
	    for (int j = i + 1; j < noteList.size(); j++) {
		if (curPitch == noteList.get(j).getPitch()) {
		    sumDuration += noteList.get(j).getRhythmValue();
		    noteList.remove(j);
		    j--;
		}
	    }
	    noRepsNoteList.add(i, new Note(curPitch, sumDuration));
	}

	Note maxLengthNote = Collections.max(noRepsNoteList,
		new Comparator<Note>() {
		    @Override
		    public int compare(Note o1, Note o2) {
			Double double1 = new Double(o1.getRhythmValue());
			Double double2 = new Double(o2.getRhythmValue());
			return double1.compareTo(double2);
		    }
		});
	return maxLengthNote.getPitch() % 12;
    }

    /**
     * Defines the octave of the backing chords
     * */
    private void fitBackingOctave() {
	Note lowestNote = Collections.min(melody, new Comparator<Note>() {
	    @Override
	    public int compare(Note o1, Note o2) {
		Double double1 = new Double(o1.getPitch());
		Double double2 = new Double(o2.getPitch());
		return double1.compareTo(double2);
	    }
	});
	octave = (int) lowestNote.getPitch() / 12 - 1;
    }

    /**
     * Adds triad or it's first inversion (sixth) to the backing chords
     * progression. In major R 35(0-4-7)may be found on I,IV and V; R b35(0-3-7)
     * builds on II,III,VI; diminished R b3b5(0-3-6) based on VII. In harmonic
     * minor R 35 builds on VI,V ; R b35 based on I and IV; R b3b5 builds on II
     * and VII; R 3d5 (0-4-8) may be found on III.
     * 
     * @param root chord's root pitch
     * @param firstInvertion
     * */
    private void triad(int root, boolean firstInvertion) {
	int third;
	int fifth;
	int[] chord = new int[3];
	if (keyQuality == 1) {
	    if (root == tonic || root == subdominant
		    || root == dominantGroup[2] || root == subdominantGroup[0])
		third = root + 3;
	    else
		third = root + 4;

	} else {
	    if (root == tonic || root == subdominant || root == dominant)
		third = root + 4;
	    else
		third = root + 3;
	}

	fifth = root + 7;
	if (keyQuality == 1) {
	    if (root == subdominantGroup[0] || root == dominantGroup[2])
		fifth = root + 6;
	    if (root == dominantGroup[0])
		fifth = root + 8;
	} else {
	    if (root == dominantGroup[2])
		fifth = root + 6;
	}
	if (firstInvertion) {
	    chord[0] = third;
	    chord[1] = fifth;
	    chord[2] = root;

	    if (root == subdominantGroup[2] || root == dominantGroup[1]
		    || root == dominantGroup[2]) {
		for (int i = 0; i < 3; i++)
		    chord[i] += 12 * (octave - 1);
	    } else {
		for (int i = 0; i < 3; i++)
		    chord[i] += 12 * octave;
	    }
	    chord[2] += 12;
	} else {
	    chord[0] = root;
	    chord[1] = third;
	    chord[2] = fifth;

	    if (root == dominantGroup[2]) {
		for (int i = 0; i < 3; i++)
		    chord[i] += 12 * (octave - 1);
	    } else {
		for (int i = 0; i < 3; i++)
		    chord[i] += 12 * octave;
	    }
	}
	backing.addChord(chord, chordDuration);

    }

    private void initializeTransitMatrix() {
	int[][] a = new int[14][14];
	a[0][1] = a[0][11] = a[0][7] = a[0][13] = 1;
	a[1][1] = a[1][2] = a[1][3] = a[1][8] = a[1][4] = a[1][7] = a[1][13] = 1;
	a[2][1] = a[2][3] = a[2][11] = a[2][7] = a[2][7] = a[2][13] = 1;
	a[3][1] = a[3][8] = a[3][9] = a[3][4] = a[3][10] = a[3][13] = 1;
	a[8][1] = a[8][11] = a[8][7] = a[8][13] = 1;
	a[9][1] = a[9][10] = a[9][6] = a[9][7] = 1;
	a[4][1] = a[4][3] = a[4][9] = a[4][13] = 1;
	a[5][1] = a[5][3] = a[5][9] = a[5][10] = a[5][6] = 1;
	a[10][1] = a[10][9] = a[10][7] = 1;
	a[11][0] = a[11][7] = a[11][12] = a[11][13] = 1;
	a[6][1] = a[6][3] = a[6][9] = a[6][5] = a[6][10] = 1;
	a[7][1] = a[7][2] = a[7][8] = a[7][11] = a[7][12] = a[7][13] = 1;
	a[12][1] = a[12][11] = a[12][7] = 1;
	a[13][0] = a[13][1] = a[13][8] = a[13][11] = a[13][7] = 1;
	transMatrix = a;
    }

    private void fitHarmony() {
	int harmonyGroup = 0;
	int prevState = 0;
	triad(tonic, false);
	for (int i = 1; i < bars.size() - 1; i++) {
	    harmonyGroup = barHarmony(bars.get(i));
	    if (harmonyGroup == tonic) {
		prevState = getNextState(prevState, 0);
	    } else {
		if (Arrays.binarySearch(subdominantGroup, harmonyGroup) >= 0) {
		    prevState = getNextState(prevState, 1);
		} else {
		    prevState = getNextState(prevState, 2);
		}
	    }
	}
	cadenza();
    }

    private void cadenza() {
	triad(subdominantGroup[0], true);
	triad(dominant, false);
	triad(tonic, false);
    }

    private int getNextState(int prevState, int harmonyGroup) {
	int sumInRow = 0;
	double[] transRow = new double[2];
	switch (harmonyGroup) {
	case 0: {
	    transRow = new double[2];
	    for (int i = 0; i < 2; i++) {
		sumInRow += transMatrix[prevState][i];
		transRow[i] = transMatrix[prevState][i];
	    }
	    break;
	}
	case 1: {
	    transRow = new double[6];
	    for (int i = 2; i < 8; i++) {
		sumInRow += transMatrix[prevState][i];
		transRow[i - 2] = transMatrix[prevState][i];
	    }
	    break;
	}
	case 2: {
	    transRow = new double[8];
	    for (int i = 8; i < 14; i++) {
		sumInRow += transMatrix[prevState][i];
		transRow[i - 8] = transMatrix[prevState][i];
	    }
	    transRow[6] = transMatrix[prevState][0];
	    sumInRow += transMatrix[prevState][0];
	    transRow[7] = transMatrix[prevState][1];
	    sumInRow += transMatrix[prevState][1];
	    break;
	}
	}
	for (int i = 0; i < transRow.length; i++) {
	    transRow[i] /= sumInRow;
	}

	double sum = 0;
	double choise = Math.random();
	for (int i = 0; i < transRow.length; i++) {
	    sum += transRow[i];
	    if (choise <= sum) {
		switch (harmonyGroup) {
		case 0: {
		    if (i == 0)
			triad(tonic, false);
		    else
			triad(tonic, true);
		    return i;
		}
		case 1: {
		    if (i % 2 == 0)
			triad(subdominantGroup[(int) i / 2], false);
		    else
			triad(subdominantGroup[(int) i / 2], true);
		    return i + 2;
		}
		case 2: {
		    if (i % 2 == 0) {
			if (i == 0) {
			    triad(tonic, false);
			    return 0;
			} else
			    triad(dominantGroup[(int) i / 2 - 1], false);
		    } else {
			if (i == 1) {
			    triad(tonic, true);
			    return 1;
			} else
			    triad(dominantGroup[(int) i / 2 - 1], true);
		    }
		    return i + 6;
		}

		}

	    }
	}
	return 0;
    }

    public Score createScore() {
	Score score = new Score();
	Part part = new Part();
	Note[] melodyNoteArray = new Note[melody.size()];
	melody.toArray(melodyNoteArray);
	Phrase melodyPhrase = new Phrase(melodyNoteArray);
	melodyPhrase.setTitle("melody");
	backing.setInstrument(0);
	backing.setDuration(chordDuration * 1.2);
	//backing.setVolume(Volume.PIANO);
	backing.setStartTime(0);
	part.add(melodyPhrase);
	part.add(backing);
	score.add(part);
	return score;
    }

    /*
     * Defines major and minor key with such key signature
     * 
     * @param keySignature
     * 
     * @return keys array 0 - major key, 1- minor key
     */

    public static int[] getKeys(int keySignature) {
	int tonic[] = new int[2];
	if (keySignature >= 0) {
	    if (keySignature % 2 == 0)
		tonic[0] = keySignature;
	    else
		tonic[0] = keySignature + 6;

	} else {
	    if (keySignature % 2 == 0)
		tonic[0] = keySignature + 12;
	    else
		tonic[0] = keySignature + 6;
	}
	if (tonic[0] >= 12 || tonic[0] < 0)
	    tonic[0] = Math.abs(12 - Math.abs(tonic[0]));

	if ((tonic[0] - 3) >= 0)
	    tonic[1] = tonic[0] - 3;
	else
	    tonic[1] = tonic[0] + 9;
	return tonic;

    }

    public static void fitToKey(ArrayList<Note> melody, int keySignature) {
	int[] naturalsToSharps = { 5, 0, 7, 2, 9, 4, 11 };
	int[] naturalsToFlats = { 11, 4, 9, 2, 7, 0, 5 };

	if (Math.abs(keySignature) > 7) {
	    throw new IllegalPieceAttributesException();
	}
	for (Note n : melody) {
	    for (int i = 0; i < Math.abs(keySignature); i++) {
		if (keySignature > 0) {
		    if (n.getPitch() % 12 == naturalsToSharps[i]) {
			n.setPitch(n.getPitch() + 1);
		    }
		}
		if (keySignature < 0) {
		    if (n.getPitch() % 12 == naturalsToFlats[i]) {
			n.setPitch(n.getPitch() - 1);
		    }
		}
	    }
	}
    }
}