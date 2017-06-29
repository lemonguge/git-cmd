
package cn.homjie.boot;

import java.io.File;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GitCmdApplication {

	private static Logger log = LoggerFactory.getLogger(GitCmdApplication.class);

	public static void main(String[] args) {

		// 编码
		// byCode();
		// 输入
		// byInput();

		byRepository();

	}

	static void byRepository() {
		File repository = new File("E:\\gitlabr\\build");
		GitExecute gitExecute = new GitRepository(repository);
//		gitExecute.register(GitConditions.gitCheckout("master", true));
		gitExecute.register(GitConditions.gitPull());
		gitExecute.exec();
	}

	static void byInput() {
		Scanner reader = new Scanner(System.in);
		log.info("enter repository directory");
		String path = reader.nextLine();

		// E:\GitLab仓库
		File dir = new File(path);

		GitExecute gitExecute = new GitHandle(dir);

		gitExecute.register(new ScannerGitCondition(reader));

		gitExecute.exec();

		reader.close();
	}

	static void byCode() {

		// E:\\GitLab仓库
		File repository = new File("E:\\gitlab");
		GitExecute gitExecute = new GitHandle(repository);

		// newBranch(gitHandle);

		// fix-transaction-too-long
		gitExecute.register(GitConditions.gitPull());
		// gitExecute.register(GitConditions.gitCheckout("master", true));
		// gitExecute.register(GitConditions.gitPull());

		// String branch = "fix-transaction-too-long";
		// gitExecute.register(GitConditions.gitBranchNew(branch));
		// gitExecute.register(GitConditions.gitCheckout(branch, true));

		// gitExecute.register(GitConditions.gitBranchTrack("master"));

		// gitExecute.register(GitConditions.gitPull());
		// gitExecute.register(GitConditions.gitCheckout("fengdai_2.6sp4_release", false));
		// gitExecute.register(GitConditions.gitPull());

		// gitExecute.register(GitConditions.gitMerge("fengdai_2.6sp4_release"));

		// gitExecute.register(GitConditions.gitBranchPush("master"));

		// gitExecute.register(GitConditions.gitBranchDelete("fengdai_2.6sp4_release", true));
		// gitExecute.register(GitConditions.gitBranchVersion());

		gitExecute.exec();

	}

	static void newBranch(GitExecute gitExecute) {
		String branch = "2.6sp5";

		gitExecute.register(GitConditions.gitPull());
		gitExecute.register(GitConditions.gitBranchNew(branch));
		gitExecute.register(GitConditions.gitCheckout(branch, true));
		gitExecute.register(GitConditions.gitBranchPush(branch));
		gitExecute.register(GitConditions.gitBranchTrack(branch));
		gitExecute.register(GitConditions.gitPull());
	}

}
