package com.endside.api;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.text.NumberFormat;
import java.util.List;
import java.util.TimeZone;

@Slf4j
@SpringBootApplication(scanBasePackages={"com.endside.api"})
public class Application {
	@PostConstruct
	void started() {
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
		printGcTypes();
		printMemoryOptions();
	}

	private static void printGcTypes(){
		List<GarbageCollectorMXBean> gcMxBeans = ManagementFactory.getGarbageCollectorMXBeans();
		gcMxBeans.forEach(gcMxBean -> log.info("GcName: {}, ObjectName: {}", gcMxBean.getName(), gcMxBean.getObjectName()));
	}

	private static void printMemoryOptions(){
		Runtime runtime = Runtime.getRuntime();
		final NumberFormat format = NumberFormat.getInstance();
		final long maxMemory = runtime.maxMemory();
		final long allocatedMemory = runtime.totalMemory();
		final long freeMemory = runtime.freeMemory();
		final long kb = 1024;
		final long mb = kb * kb;
		final String mega = " MB";
		log.info("========================== Memory Info ==========================");
		log.info("Free memory: {}" , format.format(freeMemory / mb) + mega);
		log.info("Allocated memory: {}" , format.format(allocatedMemory / mb) + mega);
		log.info("Max memory: {}" , format.format(maxMemory / mb) + mega);
		log.info("Total free memory: {}" , format.format((freeMemory + (maxMemory - allocatedMemory)) / mb) + mega);
		log.info("=================================================================\n");
	}

}
