package cn.homjie.boot;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class GitRepository extends GitExecute {

	public GitRepository(File repository) {
		if (repository.exists()) {
			if (repository.isDirectory()) {
				List<File> childDirs = Arrays.asList(repository.listFiles(f -> f.isDirectory()));
				boolean match = childDirs.stream().anyMatch(f -> ".git".equals(f.getName()));
				if (match) {
					projects.add(repository);
					project(repository, projects);
				} else {
					log.info("it is not git repository.");
				}
			} else {
				log.info("it is not directory.");
			}
		} else {
			log.info("file is not exitst.");
		}
	}

	private void project(File directory, List<File> list) {
		List<File> childFiles = Arrays.asList(directory.listFiles(f -> f.isFile()));
		boolean match = childFiles.stream().anyMatch(f -> ".git".equals(f.getName()));
		if (match) {
			list.add(directory);
			return;
		}
		List<File> childDirs = Arrays.asList(directory.listFiles(f -> f.isDirectory() && !f.getName().startsWith(".")));
		if (childDirs.isEmpty())
			return;
		// 当前目录下是否有 .git 目录
		childDirs.forEach(dir -> project(dir, list));
	}

}
