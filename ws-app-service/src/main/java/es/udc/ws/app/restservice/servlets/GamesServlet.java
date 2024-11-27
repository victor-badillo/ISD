package es.udc.ws.app.restservice.servlets;

import es.udc.ws.app.restservice.dto.GameToRestGameDtoConversor;
import es.udc.ws.app.restservice.dto.RestGameDto;
import es.udc.ws.app.restservice.json.JsonToRestGameDtoConversor;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.servlet.RestHttpServletTemplate;
import es.udc.ws.util.servlet.ServletUtils;
import game.Game;
import gameservice.GameServiceFactory;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("serial")
public class GamesServlet extends RestHttpServletTemplate {

    @Override
    protected void processPost(HttpServletRequest req, HttpServletResponse resp) throws IOException,
            InputValidationException {
        ServletUtils.checkEmptyPath(req);

        RestGameDto gameDto = JsonToRestGameDtoConversor.toRestGameDto(req.getInputStream());
        Game game = GameToRestGameDtoConversor.toGame(gameDto);

        game = GameServiceFactory.getService().addGame(game);

        gameDto = GameToRestGameDtoConversor.toRestGameDto(game);
        String gameURL = ServletUtils.normalizePath(req.getRequestURL().toString()) + "/" + game.getGameId();
        Map<String, String> headers = new HashMap<>(1);
        headers.put("Location", gameURL);
        ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_CREATED,
                JsonToRestGameDtoConversor.toObjectNode(gameDto), headers);
    }


    @Override
    protected void processGet(HttpServletRequest req, HttpServletResponse resp) throws IOException,
            InputValidationException, InstanceNotFoundException {

        if(req.getPathInfo() == null || req.getPathInfo().equals("/")){
            LocalDateTime specifiedDate = LocalDateTime.parse(req.getParameter("specifiedDate"));

            List<Game> games = GameServiceFactory.getService().findGameByDate(LocalDateTime.now().withNano(0), specifiedDate.withNano(0));

            List<RestGameDto> gameDtos = GameToRestGameDtoConversor.toRestGameDtos(games);
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_OK,
                    JsonToRestGameDtoConversor.toArrayNode(gameDtos), null);
        }else{
            Long gameId = ServletUtils.getIdFromPath(req, "game");

            Game game = GameServiceFactory.getService().findGameById(gameId);

            RestGameDto gameDto = GameToRestGameDtoConversor.toRestGameDto(game);
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_OK,
                    JsonToRestGameDtoConversor.toObjectNode(gameDto), null);
        }
    }
}