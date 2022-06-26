package com.pucp.odiparpackback.model;

import com.pucp.odiparpackback.enums.FileType;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Date;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UploadFile {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private String name;

  private Date uploadDate;

  private FileType fileType;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    UploadFile uploadFile = (UploadFile) o;
    return id != null && Objects.equals(id, uploadFile.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
