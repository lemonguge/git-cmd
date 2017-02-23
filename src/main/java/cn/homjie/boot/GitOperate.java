package cn.homjie.boot;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum GitOperate {

	GIT_PULL("git pull") {

		@Override
		boolean needBranch() {
			return false;
		}

		@Override
		boolean needLocal() {
			return false;
		}

		@Override
		Process handle(Runtime runtime, File project, String branch, boolean local) throws Exception {
			return runtime.exec("git pull", null, project);
		}

	},

	GIT_MERGE("git merge") {

		@Override
		boolean needBranch() {
			return true;
		}

		@Override
		boolean needLocal() {
			return false;
		}

		@Override
		Process handle(Runtime runtime, File project, String branch, boolean local) throws Exception {
			return runtime.exec("git merge " + branch, null, project);
		}

	},

	GIT_BRANCH_PUSH("git branch push") {

		@Override
		boolean needBranch() {
			return true;
		}

		@Override
		boolean needLocal() {
			return false;
		}

		@Override
		Process handle(Runtime runtime, File project, String branch, boolean local) throws Exception {
			return runtime.exec("git push origin " + branch, null, project);
		}

	},

	GIT_CHECKOUT("git checkout") {

		@Override
		boolean needBranch() {
			return true;
		}

		@Override
		boolean needLocal() {
			return true;
		}

		@Override
		Process handle(Runtime runtime, File project, String branch, boolean local) throws Exception {
			if (local)
				return runtime.exec("git checkout " + branch, null, project);
			else
				return runtime.exec("git checkout --track origin/" + branch, null, project);
		}

	},

	GIT_BRANCH_DELETE("git branch delete") {

		@Override
		boolean needBranch() {
			return true;
		}

		@Override
		boolean needLocal() {
			return true;
		}

		@Override
		Process handle(Runtime runtime, File project, String branch, boolean local) throws Exception {
			if (local)
				return runtime.exec("git branch -d " + branch, null, project);
			else
				return runtime.exec("git push origin --delete " + branch, null, project);

		}

	},

	GIT_BRANCH_NEW("git branch new") {

		@Override
		boolean needBranch() {
			return true;
		}

		@Override
		boolean needLocal() {
			return false;
		}

		@Override
		Process handle(Runtime runtime, File project, String branch, boolean local) throws Exception {
			return runtime.exec("git branch " + branch, null, project);
		}

	},

	GIT_BRANCH_TRACK("git branch track") {

		@Override
		boolean needBranch() {
			return true;
		}

		@Override
		boolean needLocal() {
			return false;
		}

		@Override
		Process handle(Runtime runtime, File project, String branch, boolean local) throws Exception {
			return runtime.exec("git branch -u origin/" + branch, null, project);
		}

	},

	GIT_BRANCH_VERSION("git branch version") {

		@Override
		boolean needBranch() {
			return false;
		}

		@Override
		boolean needLocal() {
			return false;
		}

		@Override
		Process handle(Runtime runtime, File project, String branch, boolean local) throws Exception {
			return runtime.exec("git branch -vv", null, project);
		}

	};

	private String code;

	private static Logger log = LoggerFactory.getLogger(GitOperate.class);

	private GitOperate(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public static String support() {
		GitOperate[] operates = GitOperate.values();
		StringBuilder choice = new StringBuilder();
		for (GitOperate operate : operates) {
			choice.append('|').append(operate.code);
		}
		return choice.substring(1).toString();
	}

	public static GitOperate support(String operate) {
		GitOperate[] operates = GitOperate.values();
		for (GitOperate o : operates) {
			if (o.code.equals(operate))
				return o;
		}
		return null;
	}

	public int exec(Runtime runtime, File project, String branch, boolean local) {

		String exec = code + " " + project.getName();
		BufferedReader stdin = null;
		BufferedReader stderr = null;
		try {
			// 处理每个项目
			Process process = handle(runtime, project, branch, local);

			stdin = new BufferedReader(new InputStreamReader(process.getInputStream()));
			stderr = new BufferedReader(new InputStreamReader(process.getErrorStream()));

			String line = null;
			while ((line = stderr.readLine()) != null)
				log.info("    " + line);
			while ((line = stdin.readLine()) != null)
				log.info("    " + line);

			int exitVal = process.waitFor();
			log.info(exec + " success[" + exitVal + "]");
			return exitVal;
		} catch (Exception e) {
			log.error(exec + " failure[" + e.getMessage() + "]");
		} finally {
			if (stdin != null) {
				try {
					stdin.close();
				} catch (Exception e) {
				}
			}
			if (stderr != null) {
				try {
					stderr.close();
				} catch (Exception e) {
				}
			}

		}
		return 0;
	}

	abstract boolean needBranch();

	abstract boolean needLocal();

	abstract Process handle(Runtime runtime, File project, String branch, boolean local) throws Exception;

}
