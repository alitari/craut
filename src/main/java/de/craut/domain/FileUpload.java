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

	@Id()
	@Column(name = "fu_id", nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(name = "fu_thread", nullable = false)
	private String thread;

	@Column(name = "fu_size", nullable = false)
	private long size;

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

	public FileUpload(String thread, long size, byte[] content, Date insertDate) {
		super();
		this.thread = thread;
		this.size = size;
		this.content = content;
		this.insertDate = insertDate;
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
}
