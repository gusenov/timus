import java.io.*;
import java.util.*;

public class Solution
{
	StreamTokenizer in;
	PrintWriter out;

	public static void main(String[] args) throws IOException
	{
		new Solution().run();
	}

	void run() throws IOException
	{
		boolean oj = System.getProperty("ONLINE_JUDGE") != null;
		Reader reader = oj ? new InputStreamReader(System.in) : new InputStreamReader(new ReverseLineInputStream(new File("input.txt")));
        Writer writer = oj ? new OutputStreamWriter(System.out) : new FileWriter("output.txt");
		in = new StreamTokenizer(new BufferedReader(reader));
		out = new PrintWriter(writer);
		solve();
		out.flush();
	}

	void solve() throws IOException
	{
		boolean eof = false;
		do
		{
			int token = in.nextToken();
			switch (token) 
			{
				case StreamTokenizer.TT_EOF:
					eof = true;
					break;
				case StreamTokenizer.TT_NUMBER:
					long n = (long)in.nval;
					out.println(String.format(Locale.US, "%.4f", Math.sqrt(n)));
					break;
				default:
			}
		} while (!eof);
	}
}

class ReverseLineInputStream extends InputStream 
{
	RandomAccessFile in;

	long currentLineStart = -1;
	long currentLineEnd = -1;
	long currentPos = -1;
	long lastPosInFile = -1;

	public ReverseLineInputStream(File file) throws FileNotFoundException 
	{
		in = new RandomAccessFile(file, "r");
		currentLineStart = file.length();
		currentLineEnd = file.length();
		lastPosInFile = file.length() - 1;
		currentPos = currentLineEnd; 
	}

	public void findPrevLine() throws IOException 
	{
		currentLineEnd = currentLineStart; 

		// There are no more lines, since we are at the beginning of the file and no lines.
		if (currentLineEnd == 0) 
		{
			currentLineEnd = -1;
			currentLineStart = -1;
			currentPos = -1;
			return; 
		}

		long filePointer = currentLineStart - 1;

		while (true) 
		{
			filePointer--;

			// We are at start of file so this is the first line in the file.
			if (filePointer < 0) 
			{  
				break; 
			}

			in.seek(filePointer);
			int readByte = in.readByte();

			// We ignore last LF in file. search back to find the previous LF.
			if ((readByte == 0xA || readByte == ' ') && filePointer != lastPosInFile)
			{   
				break;
			}
		}

		// We want to start at pointer +1 so we are after the LF we found or at 0 the start of the file.   
		currentLineStart = filePointer + 1;
		currentPos = currentLineStart;
	}

	public int read() throws IOException 
	{
		if (currentPos < currentLineEnd) 
		{
			in.seek(currentPos++);
			int readByte = in.readByte();
			return readByte;
		}
		else if (currentPos < 0) 
		{
			return -1;
		}
		else 
		{
			findPrevLine();
			return read();
		}
	}
}