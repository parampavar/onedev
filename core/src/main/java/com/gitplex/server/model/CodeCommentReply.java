package com.gitplex.server.model;

import java.util.Date;

import javax.annotation.Nullable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.DynamicUpdate;

/*
 * @DynamicUpdate annotation here along with various @OptimisticLock annotations
 * on certain fields tell Hibernate not to perform version check on those fields
 * which can be updated from background thread.
 */
@Entity
@Table(indexes={@Index(columnList="g_comment_id"), @Index(columnList="g_user_id")})
@DynamicUpdate 
public class CodeCommentReply extends AbstractEntity {
	
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(nullable=false)
	private CodeComment comment;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn
	private User user;
	
	private String userName;

	@Column(nullable=false)
	private Date date;
	
	@Version
	private long version;
	
	public CodeComment getComment() {
		return comment;
	}

	public void setComment(CodeComment comment) {
		this.comment = comment;
	}

	@Nullable
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Nullable
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public long getVersion() {
		return version;
	}

	public void setVersion(long version) {
		this.version = version;
	}
	
	public String getAnchor() {
		return getClass().getSimpleName() + "-" + getId();
	}
	
	@Lob
	@Column(nullable=false, length=65535)
	private String content;
	
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
