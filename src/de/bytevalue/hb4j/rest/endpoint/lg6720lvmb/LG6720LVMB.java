package de.bytevalue.hb4j.rest.endpoint.lg6720lvmb;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import de.bytevalue.hb4j.joystick.JoystickDirection;
import de.bytevalue.hb4j.json.JsonRequest;
import de.bytevalue.hb4j.lg6720lvmb.CleanMode;
import de.bytevalue.hb4j.rest.CommandDto;
import de.bytevalue.hb4j.rest.ParamDto;
import de.bytevalue.hb4j.rest.ResponseDto;

@Path("/lg6720lvmb")
@Singleton
public class LG6720LVMB {
	private Map<String, LG6720LVMBContainer> bots;
	
	public LG6720LVMB() {
		this.bots = new HashMap<>();
	}
	
	@GET
	@Path("{ip}/create")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCreate(@PathParam("ip") String ip) {
		if(!bots.containsKey(ip)) {
			de.bytevalue.hb4j.lg6720lvmb.LG6720LVMB bot = new de.bytevalue.hb4j.lg6720lvmb.LG6720LVMB(ip);
			this.bots.put(ip, new LG6720LVMBContainer(bot));
		}
		
		return Response.ok(new ResponseDto(ip, "Hombot instance created at " + ip)).build();
	}
	
	@GET
	@Path("/{ip}/connect")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getConnect(@PathParam("ip") String ip) throws IOException {
		if(!bots.containsKey(ip)) {
			return this.createBotNotFoundResponse(ip);
		}
		
		bots.get(ip).hombot.connect();
		
		return Response.ok(new ResponseDto(ip, "Hombot instance connected at " + ip)).build();
	}
	
	@GET
	@Path("/{ip}/disconnect")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDisconnect(@PathParam("ip") String ip) throws IOException {
		if(!bots.containsKey(ip)) {
			return this.createBotNotFoundResponse(ip);
		}
		
		bots.get(ip).hombot.disconnect();
		
		return Response.ok(new ResponseDto(ip, "Hombot instance disconnected from " + ip)).build();
	}
	
	@GET
	@Path("/{ip}/model")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getModel(@PathParam("ip") String ip) throws IOException {
		if(!bots.containsKey(ip)) {
			return this.createBotNotFoundResponse(ip);
		}
		
		de.bytevalue.hb4j.lg6720lvmb.LG6720LVMB hombot = bots.get(ip).hombot;
		
		if(!hombot.isConnected()) {
			return this.createBotNotConnectedResponse(ip);
		}
		
		return Response.ok(hombot.getModel()).build();
	}
	
	@POST
	@Path("/{ip}/raw")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response postRawCommand(@Context HttpServletRequest request, @PathParam("ip") String ip) throws IOException {
		try {
			if(!bots.containsKey(ip)) {
				return this.createBotNotFoundResponse(ip);
			}
			
			de.bytevalue.hb4j.lg6720lvmb.LG6720LVMB hombot = bots.get(ip).hombot;
			
			if(!hombot.isConnected()) {
				return this.createBotNotConnectedResponse(ip);
			}
		    
			String requestBody = this.readRequestBody(request);
			hombot.sendRequest(new JsonRequest(requestBody));
			
			return Response.ok(new ResponseDto(ip, "Raw Command " + requestBody + " was executed")).build();
		    
		}
		catch (Exception ex) {
			return this.createExceptionResponse(ip, ex);
		}
	}
	
	@POST
	@Path("/param")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response postParam(ParamDto paramDto) throws IOException {
		if(!bots.containsKey(paramDto.ip)) {
			return this.createBotNotFoundResponse(paramDto.ip);
		}
		
		de.bytevalue.hb4j.lg6720lvmb.LG6720LVMB hombot = bots.get(paramDto.ip).hombot;
		
		if(!hombot.isConnected()) {
			return this.createBotNotConnectedResponse(paramDto.ip);
		}
		
		switch(paramDto.name) {
			case "mode": {
				hombot.setCleanMode(CleanMode.valueOf(paramDto.value));
				break;
			}
			case "turbo": {
				hombot.enableTurbo(Boolean.parseBoolean(paramDto.value));
				break;
			}
			case "move-once": {
				bots.get(paramDto.ip).hombot.move(JoystickDirection.valueOf(paramDto.value));
				break;
			}
			case "move": {
				bots.get(paramDto.ip).getJoystickControl().move(JoystickDirection.valueOf(paramDto.value));
				break;
			}
			case "reservation": {
				hombot.setReservation(paramDto.reservation);
				break;
			}
		}
		
		return Response.ok(new ResponseDto(paramDto.ip, "Param " + paramDto.name + " was set")).build();
	}
	
	@POST
	@Path("/command")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response postCommand(CommandDto commandDto) throws IOException {
		if(!bots.containsKey(commandDto.ip)) {
			return this.createBotNotFoundResponse(commandDto.ip);
		}
		
		de.bytevalue.hb4j.lg6720lvmb.LG6720LVMB hombot = bots.get(commandDto.ip).hombot;
		
		if(!hombot.isConnected()) {
			return this.createBotNotConnectedResponse(commandDto.ip);
		}
		
		switch(commandDto.command) {
			case "start": {
				hombot.startClean();
				break;
			}
			case "pause": {
				hombot.pauseClean();
				break;
			}
			case "home": {
				hombot.goHome();
				break;
			}
		}
		
		return Response.ok(new ResponseDto(commandDto.ip, "Command " + commandDto.command + " was executed")).build();
	}
	
	@DELETE
	@Path("/command")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteCommand(CommandDto commandDto) throws IOException {
		if(!bots.containsKey(commandDto.ip)) {
			return this.createBotNotFoundResponse(commandDto.ip);
		}
		
		de.bytevalue.hb4j.lg6720lvmb.LG6720LVMB hombot = bots.get(commandDto.ip).hombot;
		
		if(!hombot.isConnected()) {
			return this.createBotNotConnectedResponse(commandDto.ip);
		}
		
		switch(commandDto.command) {
			case "reservation": {
				hombot.removeReservation();
				break;
			}
		}
		
		return Response.ok(new ResponseDto(commandDto.ip, "Command " + commandDto.command + " was executed")).build();
	}
	
	private Response createBotNotFoundResponse(String ip) {
		return Response.status(404).entity(new ResponseDto(ip, "No bot found at ip " + ip)).build();
	}
	
	private Response createBotNotConnectedResponse(String ip) {
		return Response.status(404).entity(new ResponseDto(ip, "Bot is not connected at ip " + ip)).build();
	}
	
	private Response createExceptionResponse(String ip, Exception ex) {
		return Response.status(500).entity(new ResponseDto(ip, "An Exception occurred " + ex.getMessage())).build();
	}
	
	private String readRequestBody(HttpServletRequest request) throws IOException {
		StringBuffer contentBuffer = new StringBuffer();
		
		BufferedReader reader = request.getReader();
		String line = null;
	    while ((line = reader.readLine()) != null) {
	    	contentBuffer.append(line);
	    }
	    
	    return contentBuffer.toString();
	}
}