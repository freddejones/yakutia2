package se.freddejones.game.yakutia.service;


import org.junit.Before;
import se.freddejones.game.yakutia.dao.PlayerDao;
import se.freddejones.game.yakutia.dao.PlayerFriendDao;
import se.freddejones.game.yakutia.entity.Player;
import se.freddejones.game.yakutia.entity.PlayerFriend;
import se.freddejones.game.yakutia.model.statuses.FriendStatus;
import se.freddejones.game.yakutia.service.impl.FriendServiceImpl;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;

public class FriendServiceTest {

    public static final long PLAYER_ID = 1L;
    public static final long FRIEND_ID = 2L;
    private PlayerFriendDao playerFriendDaoMock;
    private PlayerDao playerDaoMock;

    private FriendService friendService;

    @Before
    public void setUp() throws Exception {
        playerFriendDaoMock = mock(PlayerFriendDao.class);
        playerDaoMock = mock(PlayerDao.class);
        friendService = new FriendServiceImpl(playerDaoMock, playerFriendDaoMock);
    }

//    @Test
//    public void testGetFriendThatHaveStatusInvited() throws Exception {
//        // given
//        when(playerDaoMock.getPlayerById(PLAYER_ID)).thenReturn(createNewPlayer());
//
//        // when
//        List<Player> invitedFriends = friendService.getFriendInvites(PLAYER_ID);
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
        p.setFriendsReqested(friendInvites);
        p.setFriends(friendsAccepted);
        return p;
    }
}