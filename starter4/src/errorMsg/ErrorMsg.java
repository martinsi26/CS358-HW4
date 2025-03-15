package errorMsg;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.TreeSet;
import java.util.TreeMap;

public class ErrorMsg
{

    private TreeMap<Integer,Integer> lines;
    private TreeSet<CompMessage> messages;
    private int lineNum;
    private String file;
    public boolean anyErrors;


    public ErrorMsg(String f)
    {
        file = f;
        lines = new TreeMap<Integer,Integer>();
        lines.put(-1,0);
        lines.put(0,1);
        lineNum = 2;
        messages = new TreeSet<CompMessage>();
        anyErrors = false;
    }

    public String getFileName() {return file;}

    public void newline(int pos)
    {
        lines.put(pos,lineNum++);
    }

    public String lineAndChar(int pos)
    {
        int linePos = lines.floorKey(pos);
        return lines.get(linePos) + "." + (pos-linePos);
    }

    public void error(int pos, CompError err)
    {
        int linePos = lines.floorKey(pos);
        int line = lines.get(linePos);
        err.line = line;
        err.chr = pos-linePos;
        err.file = file;
        messages.add(err);
        anyErrors = true;
    }

    public void warning(int pos, CompWarning warn)
    {
        int linePos = lines.floorKey(pos);
        int line = lines.get(linePos);
        warn.line = line;
        warn.chr = pos-linePos;
        warn.file = file;
        messages.add(warn);
    }

    public void token(int pos, String s)
    {
        int linePos = lines.floorKey(pos);
        int line = lines.get(linePos);
        Token t = new Token(s);
        t.line = line;
        t.chr = pos-linePos;
        t.file = file;
        messages.add(t);
    }

    public void info(int pos, String s)
    {
        int linePos = lines.floorKey(pos);
        int line = lines.get(linePos);
        CompMessage m = new CompMessage(s);
        m.line = line;
        m.chr = pos-linePos;
        m.file = file;
        messages.add(m);
    }

    public void error(int pos, String s)
    {
        int linePos = lines.floorKey(pos);
        int line = lines.get(linePos);
        CompMessage m = new CompError(s);
        m.line = line;
        m.chr = pos-linePos;
        m.file = file;
        messages.add(m);
        anyErrors = true;
    }

    public void printMessages()
    {
        for(CompMessage msg : messages)
        {
            System.out.println(msg);
        }
    }
}

