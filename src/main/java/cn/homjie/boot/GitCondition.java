package cn.homjie.boot;

public interface GitCondition {

	public GitOperate operate();

	public String branch();

	public boolean isLocal();
}
