package com.andcreations.process;

import java.io.OutputStream;

import com.andcreations.lang.OutputStreamProcessRunner;

/**
 * @author Mikolaj Gucki
 */
public class ProcessRunnerProcess extends OutputStreamProcessRunner {
	/** */
	ProcessRunnerProcess(ProcessBuilder processBuilder,OutputStream stdOutput,
		OutputStream errOutput) {
	//
		super(processBuilder, stdOutput, errOutput);
	}
}
