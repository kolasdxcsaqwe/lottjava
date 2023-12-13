package com.example.tt;

//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;

class TtApplicationTests {


	public static void main(String[] args) {
		String kai="1234";
		String bet="1234";
		int type=3;//4 选 3
		System.err.println(calculateOrderAnyChoose(kai.length(),type));

	}


	//choose 用户选中 当前彩种任选几
	private static int calculateOrderAnyChoose(int choose, int need) {
		int n = 1;
		int nm = 1;
		int m = 1;

		for (int i = 0; i < choose; i++) {
			n = n * (i + 1);
			if (choose - need - i > 0) {
				nm = nm * (choose - need - i);
			}
			if (need - i > 0) {
				m = m * (need - i);
			}
		}
		return n / nm / m;
	}
}
