package pers.hhelibep.Bestful.obsolute;

import pers.hhelibep.Bestful.http.IResponse;

public class ApiFlowBuilder {

    private IResponse response =null;
    public ApiFlowBuilder create(){
        return  new ApiFlowBuilder();
    }

    public ApiFlowBuilder addRequest(IRequestAction<IResponse,Object> action,Object... args){
        response=action.execute(args);
        return this;
    }

    public ApiFlowBuilder addAssert(){
        return this;
    }
}
