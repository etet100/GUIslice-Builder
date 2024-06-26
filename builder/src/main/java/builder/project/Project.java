package builder.project;

import com.google.gson.annotations.Expose;

import builder.common.Guidelines;

public class Project {

  public Project(String fileVersion, Guidelines guidelines) {
    this.fileVersion = fileVersion;
    this.guidelines = guidelines;
  }

  @Expose()
  private String fileVersion;

  @Expose()
  private Guidelines guidelines;
}
