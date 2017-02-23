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
		byCode();
		// 输入
		// byInput();
	}

	static void byInput() {
		Scanner reader = new Scanner(System.in);
		log.info("enter repository directory");
		String path = reader.nextLine();

		// E:\GitLab仓库
		File dir = new File(path);

		GitHandle handle = new GitHandle(dir);

		handle.register(new ScannerGitCondition(reader));

		handle.exec();

		reader.close();
	}

	static void byCode() {

		// E:\\GitLab仓库
		File repository = new File("E:\\GitLab仓库");
		GitHandle gitHandle = new GitHandle(repository);

		// newBranch(gitHandle);

		gitHandle.register(GitConditions.gitPull());
		// gitHandle.register(GitConditions.gitCheckout("2.5sp6", true));
		// gitHandle.register(GitConditions.gitPull());

		// gitHandle.register(GitConditions.gitCheckout("master", true));
		// gitHandle.register(GitConditions.gitBranchTrack("master"));

		// gitHandle.register(GitConditions.gitPull());
		// gitHandle.register(GitConditions.gitCheckout("master", true));
		// gitHandle.register(GitConditions.gitPull());
		// gitHandle.register(GitConditions.gitMerge("2.5sp6"));

		// gitHandle.register(GitConditions.gitBranchPush("master"));

		// gitHandle.register(GitConditions.gitBranchDelete("2.5sp6", true));

		// gitHandle.register(GitConditions.gitBranchVersion());

		gitHandle.exec();

	}

	static void newBranch(GitHandle gitHandle) {
		String branch = "2.5sp6";

		gitHandle.register(GitConditions.gitPull());
		gitHandle.register(GitConditions.gitBranchNew(branch));
		gitHandle.register(GitConditions.gitCheckout(branch, true));
		gitHandle.register(GitConditions.gitBranchPush(branch));
		gitHandle.register(GitConditions.gitBranchTrack(branch));
	}

}
