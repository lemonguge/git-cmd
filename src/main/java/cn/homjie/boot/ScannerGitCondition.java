package cn.homjie.boot;

import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScannerGitCondition implements GitCondition {

	private static Logger log = LoggerFactory.getLogger(ScannerGitCondition.class);

	private Scanner in;

	public ScannerGitCondition(Scanner in) {
		this.in = in;
	}

	@Override
	public GitOperate operate() {
		log.info("enter git operate[" + GitOperate.support() + "]");
		String o = in.nextLine();
		GitOperate operate = GitOperate.support(o);
		if (operate == null) {
			log.info("don't support " + o);
		}
		return operate;
	}

	@Override
	public String branch() {
		log.info("enter the branch to be operate");
		return in.nextLine();
	}

	@Override
	public boolean isLocal() {
		log.info("enter the branch is local? Y OR N");
		String choice = null;
		while ((choice = in.nextLine()) != null) {
			if ("y".equals(choice) || "Y".equals(choice))
				return true;
			if ("n".equals(choice) || "N".equals(choice)) {
				return false;
			}
		}
		throw new RuntimeException();
	}

}
