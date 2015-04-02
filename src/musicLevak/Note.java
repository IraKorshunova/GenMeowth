package musicLevak;

import generator.Equalable;

@SuppressWarnings("serial")
public class Note extends jm.music.data.Note implements Cloneable, Equalable {

    public Note(jm.music.data.Note note) {
	this.setDuration(note.getDuration());
	this.setDynamic(note.getDynamic());
	this.setFrequency(note.getFrequency());
	this.setMyPhrase(note.getMyPhrase());
	this.setOffset(note.getOffset());
	this.setPan(note.getPan());
	this.setPitch(note.getPitch());
	this.setPitchType(note.getPitchType());
	this.setRhythmValue(note.getRhythmValue());
	this.setSampleStartTime(note.getSampleStartTime());
    }

    public Note() {
	super();
    }

    public Note(int pitch, double rhythmValue) {
	super(pitch, rhythmValue);
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj) {
	    return true;
	}
	if ((obj == null) || !(obj instanceof Note)) {
	    return false;
	}
	Note n = (Note) obj;
	if (n.getPitch() == this.getPitch()
		&& n.getDynamic() == this.getDynamic()
		&& n.getDuration() == this.getDuration()
		&& n.isRest() == this.isRest()
		&& n.getFrequency() == this.getFrequency()
		&& n.getPitch() == this.getPitch()
		&& n.getRhythmValue() == this.getRhythmValue()) {
	    return true;
	}
	return false;
    }

    @Override
    public Note clone() {
	try {
	    Note clone = null;
	    clone = (Note) super.clone();
	    return clone;
	} catch (CloneNotSupportedException e) {
	    throw new InternalError(); // Its impossible
	}
    }
}