package cn.homjie.boot;

public class GitConditions {

	/**
	 * @Title gitPull
	 * @Description 更新当前分支
	 * @Author JieHong
	 * @Date 2017年1月17日 上午10:15:39
	 * @return
	 */
	public static GitCondition gitPull() {
		return new DefaultGitCondition(GitOperate.GIT_PULL);
	}

	/**
	 * @Title gitMerge
	 * @Description 合并当前分支
	 * @Author JieHong
	 * @Date 2017年1月17日 上午10:15:39
	 * @return
	 */
	public static GitCondition gitMerge(String branch) {
		return new DefaultGitCondition(GitOperate.GIT_MERGE, branch);
	}

	/**
	 * @Title gitBranchPush
	 * @Description 推送分支到远程
	 * @Author JieHong
	 * @Date 2017年1月17日 上午10:13:16
	 * @param branch
	 * @return
	 */
	public static GitCondition gitBranchPush(String branch) {
		return new DefaultGitCondition(GitOperate.GIT_BRANCH_PUSH, branch);
	}

	/**
	 * @Title gitCheckout
	 * @Description 检出分支
	 * @Author JieHong
	 * @Date 2017年1月17日 上午10:13:41
	 * @param branch
	 * @param local
	 *            true=本地，false=远程
	 * @return
	 */
	public static GitCondition gitCheckout(String branch, boolean local) {
		return new DefaultGitCondition(GitOperate.GIT_CHECKOUT, branch, local);
	}

	/**
	 * @Title gitBranchDelete
	 * @Description 删除分支
	 * @Author JieHong
	 * @Date 2017年1月17日 上午10:14:04
	 * @param branch
	 * @param local
	 *            true=本地，false=远程
	 * @return
	 */
	public static GitCondition gitBranchDelete(String branch, boolean local) {
		return new DefaultGitCondition(GitOperate.GIT_BRANCH_DELETE, branch, local);
	}

	/**
	 * @Title gitBranchNew
	 * @Description 新建本地分支
	 * @Author JieHong
	 * @Date 2017年1月17日 上午10:14:22
	 * @param branch
	 * @return
	 */
	public static GitCondition gitBranchNew(String branch) {
		return new DefaultGitCondition(GitOperate.GIT_BRANCH_NEW, branch);
	}

	/**
	 * @Title gitBranchTrack
	 * @Description 跟踪远程分支
	 * @Author JieHong
	 * @Date 2017年1月17日 上午10:14:34
	 * @param branch
	 * @return
	 */
	public static GitCondition gitBranchTrack(String branch) {
		return new DefaultGitCondition(GitOperate.GIT_BRANCH_TRACK, branch);
	}

	/**
	 * @Title gitBranchVersion
	 * @Description 当前分支信息
	 * @Author JieHong
	 * @Date 2017年1月17日 上午10:15:39
	 * @return
	 */
	public static GitCondition gitBranchVersion() {
		return new DefaultGitCondition(GitOperate.GIT_BRANCH_VERSION);
	}

	private static class DefaultGitCondition implements GitCondition {

		private GitOperate operate;

		private String branch;

		private boolean local;

		public DefaultGitCondition(GitOperate operate) {
			this.operate = operate;
		}

		public DefaultGitCondition(GitOperate operate, String branch) {
			this.operate = operate;
			this.branch = branch;
		}

		public DefaultGitCondition(GitOperate operate, String branch, boolean local) {
			this.operate = operate;
			this.branch = branch;
			this.local = local;
		}

		@Override
		public String branch() {
			return branch;
		}

		@Override
		public boolean isLocal() {
			return local;
		}

		@Override
		public GitOperate operate() {
			return operate;
		}

	}

}
