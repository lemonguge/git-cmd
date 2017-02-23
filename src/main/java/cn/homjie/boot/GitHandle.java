package cn.homjie.boot;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GitHandle {

	private static Logger log = LoggerFactory.getLogger(GitHandle.class);

	// 最大并发线程数
	private static final int MAX_THREAD_NUM = 6;

	private List<File> projects = new ArrayList<File>();

	private List<GitCondition> chain = new ArrayList<GitCondition>();

	public GitHandle(File repository) {
		if (repository.exists()) {
			if (repository.isDirectory()) {
				project(repository, projects);
			} else {
				log.info("it is not directory.");
			}
		} else {
			log.info("file is not exitst.");
		}
	}

	private void project(File directory, List<File> list) {
		List<File> childDirs = Arrays.asList(directory.listFiles(f -> f.isDirectory()));
		if (childDirs.isEmpty())
			return;
		// 当前目录下是否有 .git 目录
		boolean match = childDirs.stream().anyMatch(f -> ".git".equals(f.getName()));
		if (match) {
			list.add(directory);
			return;
		}
		childDirs.forEach(dir -> project(dir, list));
	}

	public GitHandle register(GitCondition condition) {
		chain.add(condition);
		return this;
	}

	public List<File> projects() {
		return projects;
	}

	public void exec() {
		int size = projects.size();

		if (size == 0) {
			log.info("no project.");
			return;
		}

		Runtime runtime = Runtime.getRuntime();
		// 并发处理 git 命令
		ExecutorService service = Executors.newFixedThreadPool(size >= MAX_THREAD_NUM ? MAX_THREAD_NUM : size);

		chain.forEach(condition -> {
			GitOperate operate = condition.operate();
			if (operate == null)
				return;

			log.info("git operate[" + operate.getCode() + "]");
			String branch = null;
			if (operate.needBranch())
				branch = condition.branch();
			boolean local = false;
			if (operate.needLocal()) {
				local = condition.isLocal();
			}

			final String b = branch;
			final boolean l = local;

			// 多线程处理
			CountDownLatch latch = new CountDownLatch(size);

			projects.forEach(project -> {
				Runnable task = () -> {
					try {
						operate.exec(runtime, project, b, l);
					} finally {
						// 无论是否正常，都需要执行
						latch.countDown();
					}
				};
				service.execute(task);
			});

			try {
				latch.await();
			} catch (InterruptedException e) {
			}

		});

		service.shutdown();
	}

}
