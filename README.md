# Vemmio tunnel pool

This is piece of code which should be responsible for creating tunnels.<br>
Tunnel is a connection between device and server, each tunnel has unique TunnelId.<br>
TunnelPool is responsible for managing Tunnels. 

TunnelPool requirements:
<ul>
<li> It should be only one Tunnel with specific TunnelId at the time inside the pool.
<li> Tunnel should be reused when already exists
<li> There could be error when trying create the Tunnel
<li> Pool should be thread safe
<li> Getting Tunnel from the pool should be non blocking
<li> All the tunnels should be closed when pool is closed (shutdown)
</ul>

## Components
<ul>
<li>App - main program
<li>Tunnel - Tunnel betwen device and server
<li>TunnelId - Id of the tunnel
<li>TunnelFactory - Factory for Tunnel with specific TunnelId
<li>TunnelPool - Thread safe, non blocking pool of tunnels
</ul>

## Building and running

mvn clean install

## Prerequisites

<ul>
<li>Java 8
<li>Maven 3
</ul>

  
 
