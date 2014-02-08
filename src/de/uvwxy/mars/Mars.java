package de.uvwxy.mars;

import java.util.HashMap;

import android.util.Log;

public class Mars {
	public static int CHUNK_N;

	MarsGenerator gen = null;
	HashMap<MarsChunkID, MarsChunk> chunks = null;

	// LinkedList<MarsChunk> chunks = null;

	public Mars(MarsGenerator gen, int n) {
		this.CHUNK_N = n;
		this.gen = gen;
		chunks = new HashMap<MarsChunkID, MarsChunk>();
	}

	public int getHeight(int x, int y) {
		int mx = x < 0 ? -x % CHUNK_N : x % CHUNK_N;
		int my = y < 0 ? -y % CHUNK_N : y % CHUNK_N;

		x /= CHUNK_N;
		y /= CHUNK_N;

		if (mx < 0)
			mx = CHUNK_N - mx - 1;
		if (my < 0)
			my = CHUNK_N - my - 1;

		if ((mx < 0 || mx > CHUNK_N) || (my < 0 || my > CHUNK_N))
			Log.i("MARS", "mx/my = " + mx + "/" + my);

		return getChunk(new MarsChunkID(x, y)).getHeight(mx, my);
	}

	public HashMap<MarsChunkID, MarsChunk> getChunks() {
		return chunks;
	}
	
	public MarsChunk getChunk(MarsChunkID id) {
		if (!contains(id)) {
			generate(id);
		}
		return chunks.get(id);
	}

	public void generate(MarsChunkID id) {
		if (!contains(id)) {
			MarsChunk c = new MarsChunk(gen, CHUNK_N, id);
			chunks.put(c.getId(), c);
		}
	}

	public boolean contains(MarsChunkID id) {
		return chunks.containsKey(id);
	}
	
	public void clearScreenData(){
		for (MarsChunk c : chunks.values()){
			c.setScreen_data(null);
		}
	}
}
