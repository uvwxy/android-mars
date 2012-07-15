package de.uvwxy.mars;

import java.util.HashMap;
import java.util.LinkedList;

public class Mars {
	private static int CHUNK_N;

	MarsGenerator gen = null;
	HashMap<MarsChunkID, MarsChunk> chunks = null;

	// LinkedList<MarsChunk> chunks = null;

	public Mars(MarsGenerator gen, int n) {
		this.CHUNK_N = n;
		this.gen = gen;
		chunks = new HashMap<MarsChunkID, MarsChunk>();
	}

	public void generate(MarsChunkID id) {
		if (!contains(id)) {
			// generate
			MarsChunk c = new MarsChunk(gen, CHUNK_N, id);
			chunks.put(c.getId(), c);
		}
	}

	public boolean contains(MarsChunkID id) {
		return chunks.containsKey(id);
	}
}
