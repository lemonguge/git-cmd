package cn.homjie.boot;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class GitHandle extends GitExecute {

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

}
