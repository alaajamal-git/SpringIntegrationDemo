package com.jms.system.metrics;

import org.springframework.stereotype.Component;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;

@Component
public class UserMetrics {
	private MeterRegistry registry;

	private Counter sign_up_success_count;
	private Counter sign_up_fail_count;
	private Counter sign_in_success_count;
	private Counter sign_in_faild_count;

	public UserMetrics(MeterRegistry registry) {
		this.registry = registry;
		initCounters();
	}

	private void initCounters() {
		sign_up_success_count = registry.counter("sign_up_success_count");
		sign_up_fail_count = registry.counter("sign_up_fail_count");
		sign_in_success_count = registry.counter("sign_in_success_count");
		sign_in_faild_count = registry.counter("sign_in_faild_count");
	}

	public void increaseCounter_1() {
		sign_in_success_count.increment();
	}

	public void increaseCounter_2() {
		sign_in_faild_count.increment();
	}

	public void increaseCounter_3() {
		sign_up_success_count.increment();
	}

	public void increaseCounter_4() {
		sign_up_fail_count.increment();
	}
}
