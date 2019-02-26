package com.yuhlearn.datastructures;

public class TailData<E>
{
    byte[] suffix;
    private ResultSet<E> data;

    public TailData( byte[] initialSuffix, ResultSet<E> initialData )
    {
        suffix = initialSuffix;
        data = initialData;
    }

    public TailData( byte[] initialSuffix, E initialData )
    {
        suffix = initialSuffix;
        data = new ResultSet<E>();
        data.add( initialData );
    }

    public void setSuffix( byte[] newSuffix )
    {
        suffix = newSuffix;
    }

    public byte[] getSuffix()
    {
        return suffix;
    }

    public void insert( E newData )
    {
        data.add( newData );
    }

    public ResultSet<E> getData()
    {
        return data;
    }
}