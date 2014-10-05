package de.craut.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "fileupload")
public class FileUpload implements Serializable {

	public static enum Type {
		Activity, Route;
	}

	public static enum Format {
		GPX;
	}

	@Id()
	@Column(name = "fu_id", nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(name = "fu_thread", nullable = false)
	private String thread;

	@Column(name = "fu_size", nullable = false)
	private long size;

	@Column(name = "fu_type", nullable = false)
	private Type type;

	@Column(name = "fu_format", nullable = false)
	private Format format;

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	@Column(name = "fu_content", nullable = false)
	@Lob
	private byte[] content;

	@Column(name = "fu_inserted", nullable = false)
	private Date insertDate;

	protected FileUpload() {
		super();
	}

	public FileUpload(String thread, long size, byte[] content, Date insertDate, Type type, Format format) {
		super();
		this.thread = thread;
		this.size = size;
		this.content = content;
		this.insertDate = insertDate;
		this.type = type;
		this.format = format;
	}

	public long getId() {
		return id;
	}

	public String getThread() {
		return thread;
	}

	public void setThread(String thread) {
		this.thread = thread;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	public Date getInsertDate() {
		return insertDate;
	}

	public void setInsertDate(Date insertDate) {
		this.insertDate = insertDate;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Format getFormat() {
		return format;
	}

	public void setFormat(Format format) {
		this.format = format;
	}

}
