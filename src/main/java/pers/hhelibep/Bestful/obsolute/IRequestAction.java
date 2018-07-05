package pers.hhelibep.Bestful.obsolute;

public interface IRequestAction<R,T> {
    R execute(T... ts);
}
