package krati.sos;

import java.io.IOException;

/**
 * ObjectCacheAgent
 * 
 * @author jwu
 *
 * @param <T> Object to be cached
 */
public class ObjectCacheAgent<T> implements ObjectCache<T>
{
    protected ObjectCache<T> _cache;
    protected ObjectHandler<T> _inboundHandler;
    protected ObjectHandler<T> _outboundHandler;
    
    public ObjectCacheAgent(ObjectCache<T> cache,
                            ObjectHandler<T> inboundHandler,
                            ObjectHandler<T> outboundHandler)
    {
        this._cache = cache;
        this._inboundHandler = inboundHandler;
        this._outboundHandler = outboundHandler;
    }
    
    public ObjectCache<T> getObjectCache()
    {
        return _cache;
    }
    
    public ObjectHandler<T> getInboundHandler()
    {
        return _inboundHandler;
    }
    
    public ObjectHandler<T> getOutboundHandler()
    {
        return _outboundHandler;
    }
    
    @Override
    public int getObjectIdCount()
    {
        return _cache.getObjectIdCount();
    }
    
    @Override
    public int getObjectIdStart()
    {
        return _cache.getObjectIdStart();
    }
    
    @Override
    public void delete(int objectId, long scn) throws Exception
    {
        _cache.delete(objectId, scn);
    }
    
    @Override
    public void set(int objectId, T object, long scn) throws Exception
    {
        if(object != null && _inboundHandler != null)
        {
            _inboundHandler.process(object);
        }
        _cache.set(objectId, object, scn);
    }
    
    @Override
    public T get(int objectId)
    {
        T object = _cache.get(objectId);
        if(object != null && _outboundHandler != null)
        {
            _outboundHandler.process(object);
        }
        return object;
    }
    
    @Override
    public void persist() throws IOException
    {
        _cache.persist();
    }
}
