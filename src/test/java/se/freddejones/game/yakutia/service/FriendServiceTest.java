package se.freddejones.game.yakutia.service;


import org.junit.Before;
import org.junit.Test;
import se.freddejones.game.yakutia.dao.PlayerDao;
import se.freddejones.game.yakutia.dao.PlayerFriendDao;
import se.freddejones.game.yakutia.entity.Player;
import se.freddejones.game.yakutia.entity.PlayerFriend;
import se.freddejones.game.yakutia.model.dto.FriendDTO;
import se.freddejones.game.yakutia.model.PlayerId;
import se.freddejones.game.yakutia.model.statuses.FriendStatus;
import se.freddejones.game.yakutia.service.impl.DefaultFriendService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class FriendServiceTest {

    public static final PlayerId PLAYER_ID = new PlayerId(1L);
    private PlayerFriendDao playerFriendDaoMock;
    private PlayerDao playerDaoMock;
    private Player playerMock;

    private FriendService friendService;

    @Before
    public void setUp() throws Exception {
        playerFriendDaoMock = mock(PlayerFriendDao.class);
        playerDaoMock = mock(PlayerDao.class);
        playerMock = mock(Player.class);
        friendService = new DefaultFriendService(playerDaoMock, playerFriendDaoMock);
    }

    @Test
    public void shouldFetchInvitedFriends() {
        // given
        when(playerDaoMock.getPlayerById(any(PlayerId.class))).thenReturn(playerMock);
        Set<PlayerFriend> invites = getPlayerFriends(FriendStatus.INVITED);
        when(playerMock.getFriends()).thenReturn(new HashSet<>());
        when(playerMock.getFriendRequests()).thenReturn(invites);

        // when
        List<FriendDTO> players = friendService.getAllFriendInvitesForPlayer(new PlayerId(1L));

        // then
        assertThat(players.size(), is(1));
    }


    @Test
    public void shouldFetchAllAcceptedFriends() {
        // given
        when(playerDaoMock.getPlayerById(any(PlayerId.class))).thenReturn(playerMock);
        Set<PlayerFriend> acceptedFriends = getPlayerFriends(FriendStatus.ACCEPTED);
        when(playerMock.getFriends()).thenReturn(acceptedFriends);

        // when
        List<FriendDTO> acceptedFriendsForPlayer = friendService.getAllAcceptedFriendsForPlayer(PLAYER_ID);

        // then
        assertThat(acceptedFriendsForPlayer.size(), is(1));
    }

    @Test
    public void shouldInvitePlayerToBeFriend() {
        // given
        when(playerDaoMock.getPlayerById(any(PlayerId.class))).thenReturn(mock(Player.class));

        // when
        friendService.inviteFriend(new PlayerId(1L), new PlayerId(2L));

        // then
        verify(playerDaoMock, times(2)).getPlayerById(any(PlayerId.class));
        verify(playerFriendDaoMock, times(1)).persistPlayerFriendEntity(any(PlayerFriend.class));
        verify(playerDaoMock, times(2)).mergePlayer(any(Player.class));
    }

    private Set<PlayerFriend> getPlayerFriends(FriendStatus status) {
        Set<PlayerFriend> playerFriends = new HashSet<>();
        playerFriends.add(createPlayerFriend(status));
        return playerFriends;
    }

    private PlayerFriend createPlayerFriend(FriendStatus status) {
        PlayerFriend playerFriend = new PlayerFriend();
        playerFriend.setFriend(mock(Player.class));
        playerFriend.setPlayer(mock(Player.class));
        playerFriend.setFriendStatus(status);
        return playerFriend;
    }

    //    @Test
//    public void testGetFriendThatHaveStatusInvited() throws Exception {
//        // given
//        when(playerDaoMock.getPlayerById(PLAYER_ID)).thenReturn(createNewPlayer());
//
//        // when
//        List<Player> invitedFriends = friendService.getAllFriendInvitesForPlayer(PLAYER_ID);
//
//        // then
//        assertThat(invitedFriends.size(), is(1));
//    }
//
//    @Test
//    public void testGetFriendThatHaveStatusAccepted() throws Exception {
//        // given
//        when(playerDaoMock.getPlayerById(PLAYER_ID)).thenReturn(createNewPlayer());
//
//        // when
//        List<Player> acceptedFriends = friendService.getFriends(PLAYER_ID);
//
//        // then
//        assertThat(acceptedFriends.size(), is(1));
//    }
//
//    @Test
//    public void testReturnFalseForNonSuccessFriendDecline() throws Exception {
//        // given
//        PlayerFriend pf = new PlayerFriend();
//        pf.setPlayerFriendId(66L);
//        when(playerFriendDaoMock.getPlayerFriend(FRIEND_ID, PLAYER_ID)).thenReturn(pf).thenReturn(pf);
//
//        // when
//        boolean returnValue = friendService.declineFriendInvite(PLAYER_ID, FRIEND_ID);
//
//        // then
//        assertThat(returnValue, is(false));
//    }

    private Player createNewPlayer() {

        PlayerFriend playerFriend = new PlayerFriend();
        playerFriend.setFriendStatus(FriendStatus.INVITED);
        Set<PlayerFriend> friendInvites = new HashSet<>();
        friendInvites.add(playerFriend);

        PlayerFriend friend = new PlayerFriend();
        friend.setFriendStatus(FriendStatus.ACCEPTED);
        Set<PlayerFriend> friendsAccepted = new HashSet<>();
        friendsAccepted.add(friend);

        Player p = new Player();
        p.setPlayerId(1L);
        p.setFriendRequests(friendInvites);
        p.setFriends(friendsAccepted);
        return p;
    }
}